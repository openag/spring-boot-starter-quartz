# spring-boot-starter-quartz

Spring Boot Starter for Quartz Scheduler (http://quartz-scheduler.org)

Current Quartz version: 2.2.3

# Configuration

TODO:

## Parameters

TODO:

## Specify Custom Executor

TODO:


# Liquibase Files

The project contains Liquibase (http://www.liquibase.org) file that will create/update all necessary database
objects for quartz. Import openag/boot/quartz/liquibase/master.xml Liquibase definition file to your master 
file to get all necessary tables created or updated accordingly.

> &lt;include file="openag/boot/quartz/liquibase/master.xml"/&gt;
