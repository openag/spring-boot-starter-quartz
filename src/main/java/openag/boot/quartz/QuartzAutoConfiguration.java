package openag.boot.quartz;

import openag.db.DBUtil;
import openag.db.DatabaseType;
import org.quartz.impl.jdbcjobstore.*;
import org.quartz.impl.jdbcjobstore.oracle.OracleDelegate;
import org.quartz.spi.JobFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static openag.db.DBUtil.withConnection;

/**
 * Spring Boot auto configuration for Quartz Scheduler
 * <p/>
 *
 * @author Andrei Maus
 */

@Configuration
@EnableConfigurationProperties(QuartzProperties.class)
@ConditionalOnClass(org.quartz.Scheduler.class)
public class QuartzAutoConfiguration {
  private static final Logger log = LoggerFactory.getLogger(QuartzAutoConfiguration.class);

  @Autowired
  private QuartzProperties properties;

  @Autowired
  private ApplicationContext context;

  @Bean
  public SchedulerFactoryBean SchedulerFactoryBean(JobFactory jobFactory, ExecutorFactory executorFactory) {
    final Properties prop = new Properties();

    final SchedulerFactoryBean bean = new SchedulerFactoryBean();
    bean.setQuartzProperties(prop);

    if (properties.isPersistent()) {
      final DataSource dataSource = getDataSource();
      bean.setDataSource(dataSource);

      // if delegate class not set detect it automatically based on the database type
      if (StringUtils.isEmpty(properties.getDriverDelegateClass())) {
        prop.setProperty("org.quartz.jobStore.driverDelegateClass",
            pickDriverDelegateClass(dataSource).getName());
      } else {
        prop.setProperty("org.quartz.jobStore.driverDelegateClass", properties.getDriverDelegateClass());
      }
    }

    bean.setTaskExecutor(executorFactory.getExecutor());

    bean.setJobFactory(jobFactory);

    bean.setWaitForJobsToCompleteOnShutdown(true);

    prop.setProperty("org.quartz.scheduler.skipUpdateCheck", "true");
    prop.setProperty("org.quartz.scheduler.instanceId", properties.getInstanceId());

    if (properties.isPersistent()) {
      prop.setProperty("org.quartz.jobStore.isClustered", String.valueOf(properties.isClustered()));
      prop.setProperty("org.quartz.jobStore.useProperties", String.valueOf(properties.isUseProperties()));
    }

    return bean;
  }

  private Class<? extends DriverDelegate> pickDriverDelegateClass(final DataSource dataSource) {
    final DatabaseType type;
    try {
      type = withConnection(dataSource, DBUtil::detectType);
    } catch (SQLException e) {
      log.error("Failed to identify database type, default driver delegate will be used: "
          + StdJDBCDelegate.class.getName(), e);
      return StdJDBCDelegate.class;
    }

    if (type == null) {
      return StdJDBCDelegate.class;
    }

    switch (type) {
      case POSTGRESQL:
        return PostgreSQLDelegate.class;
      case MSSQL:
        return MSSQLDelegate.class;
      case SYBASE:
        return SybaseDelegate.class;
      case ORACLE:
        return OracleDelegate.class;
      case HSQLDB:
        return HSQLDBDelegate.class;
      default:
        return StdJDBCDelegate.class;
    }
  }

  private DataSource getDataSource() {
    final DataSource dataSource = context.getBean(DataSource.class); //todo: add support for multiple DS
    if (dataSource == null) {
      throw new BeanInitializationException("No DataSource is configured! " +
          "DataSource bean must be configured in order to use Quartz in persistent mode");
    }
    return dataSource;
  }

  @Bean
  @ConditionalOnMissingBean(JobFactory.class)
  public JobFactory jobFactory() {
    return (bundle, scheduler) -> context.getBean(bundle.getJobDetail().getJobClass());
  }

  @Bean
  @ConditionalOnMissingBean(ExecutorFactory.class)
  public ExecutorFactory executorFactory() {
    return this::createDefaultExecutor;
  }

  private Executor createDefaultExecutor() {
    final int threadCount = properties.getThreadCount();

    if (threadCount <= 0) {
      return Executors.newCachedThreadPool();
    }

    return Executors.newFixedThreadPool(threadCount);
  }

}
