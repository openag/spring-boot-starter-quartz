package openag.boot.quartz;

import org.quartz.impl.jdbcjobstore.DriverDelegate;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Quartz spring boot configuration properties
 * <p>
 * Original quartz properties reference: http://www.quartz-scheduler.org/documentation/quartz-2.2.x/configuration/
 *
 * @author Andrei Maus
 */
@ConfigurationProperties(prefix = QuartzProperties.PREFIX)
public class QuartzProperties {

  public static final String PREFIX = "quartz";

  /**
   * See 'org.quartz.scheduler.instanceId' in quartz configuration reference
   */
  private String instanceId = "AUTO";

  /**
   * true if quartz persistent store must be used; false otherwise
   */
  private boolean persistent = false;

  /**
   * For the persistent store only, quartz driver {@link DriverDelegate} implementation See
   * http://www.quartz-scheduler.org/documentation/quartz-2.2.x/configuration/ConfigJobStoreCMT.html for available
   * options. If value is not set, database jdbc connection will be examined to identify the database type and pick the
   * suitable implementation
   */
  private String driverDelegateClass;

  /**
   * See 'org.quartz.jobStore.useProperties' in quartz configuration reference
   */
  private boolean useProperties = true;

  /**
   * See 'org.quartz.jobStore.isClustered' in quartz configuration reference
   */
  private boolean clustered = false;

  /**
   * (Max) Number of threads to be used to quartz executor. Default implementation will return {@link
   * java.util.concurrent.Executors#newCachedThreadPool()} if values is less-equals to 0 and {@link
   * java.util.concurrent.Executors#newFixedThreadPool(int)} if value is greater than 0
   */
  private int threadCount = -1;

  public String getInstanceId() {
    return instanceId;
  }

  public void setInstanceId(final String instanceId) {
    this.instanceId = instanceId;
  }

  public boolean isPersistent() {
    return persistent;
  }

  public void setPersistent(final boolean persistent) {
    this.persistent = persistent;
  }

  public String getDriverDelegateClass() {
    return driverDelegateClass;
  }

  public void setDriverDelegateClass(final String driverDelegateClass) {
    this.driverDelegateClass = driverDelegateClass;
  }

  public boolean isUseProperties() {
    return useProperties;
  }

  public void setUseProperties(final boolean useProperties) {
    this.useProperties = useProperties;
  }

  public boolean isClustered() {
    return clustered;
  }

  public void setClustered(final boolean clustered) {
    this.clustered = clustered;
  }

  public int getThreadCount() {
    return threadCount;
  }

  public void setThreadCount(final int threadCount) {
    this.threadCount = threadCount;
  }
}
