# spring-boot-starter-quartz

Spring Boot Starter for Quartz Scheduler (http://quartz-scheduler.org)

Current Quartz version: 2.2.3

# Configuration

Add the latest spring-boot-starter-quartz to your Spring Boot application, it will automatically configure
in-memory quartz scheduler. 

    openag:spring-boot-starter-quartz

## Parameters

<table>
    <tr>
        <th>Name</th>
        <th>Description</th>
        <th>Default Value</th>
    </tr>
    <tr>
        <td>quartz.instance-id</td>
        <td>See 'org.quartz.scheduler.instanceId' in quartz configuration reference</td>
        <td>AUTO</td>
    </tr>
    <tr>
        <td>quartz.persistent</td>
        <td>true if quartz persistent store must be used; false otherwise</td>
        <td>false</td>
    </tr>
    <tr>
        <td>quartz.driver-delegate-class</td>
        <td>
            For the persistent store only, quartz driver {@link DriverDelegate} implementation See
            http://www.quartz-scheduler.org/documentation/quartz-2.2.x/configuration/ConfigJobStoreCMT.html for available
            options. If value is not set, database jdbc connection will be examined to identify the database type and pick the
            suitable implementation
        </td>
        <td></td>
    </tr>
    <tr>
        <td>quartz.use-properties</td>
        <td>See 'org.quartz.jobStore.useProperties' in quartz configuration reference</td>
        <td>true</td>
    </tr>
    <tr>
        <td>quartz.clustered</td>
        <td>See 'org.quartz.jobStore.isClustered' in quartz configuration reference</td>
        <td>false</td>
    </tr>
    <tr>
        <td>quartz.thread-count</td>
        <td>Max number of threads in default quartz executor thread pool. Not applicable if custom executor is used (see below)</td>
        <td>-1</td>
    </tr>
</table>


## Custom Executor

By default, quartz will use own **java.util.concurrent.Executor** instance to run the jobs (cached executor pool if thread-count<=0 or
fixed pool if thread-count>0 with the thread count as parameter). Supply your **openag.boot.quartz.ExecutorFactory** instance in order 
to provide your own executor instance.

## Custom JobFactory

By default, quartz will fetch job instances from the current bean factory usign the class name. Supply your 
**org.quartz.spi.JobFactory** to override that behavior


# Liquibase Files

The project contains Liquibase (http://www.liquibase.org) file that will create/update all necessary database
objects for quartz. Import openag/boot/quartz/liquibase/master.xml Liquibase definition file to your master 
file to get all necessary tables created or updated accordingly.

> &lt;include file="openag/boot/quartz/liquibase/master.xml"/&gt;
