LUNCHY
======

Lunchy is an application to provide lunch location information.

START LUNCHY (the easy way)
===========================

1.) Make sure you've java, mvn and docker installed

2.) run `./run_local.sh -f`

3.) Browse to http://localhost:8080 

LET IT RUN AGAINST A LOCAL MYSQL
================================

1.) Start a local mysql server with an empty database called "MYSQL_SCHEMA" (MYSQL_USER/MYSQL_PASSWORD)

2.) mvn -Dlunchy.db.user=MYSQL_USER -Dlunchy.db.user=MYSQL_PASSWORD -Dlunchy.db.schema=MYSQL_SCHEMA "-Dlunchy.picturedir=$TMPDIR" -DcreateTables=true jetty:run

3.) Browse to http://localhost:8080 


LET IT RUN AGAINST A LOCAL HSQLDB
================================= 

1.) mvn -Dlunchy.db.user=SA -Dlunchy.db.url=jdbc:hsqldb:file: -Dlunchy.db.schema=lunchydb -Dlunchy.db.driver=org.hsqldb.jdbc.JDBCDriver -DcreateTables=true process-resources && mvn -Dlunchy.db.user=SA -Dlunchy.db.url=jdbc:hsqldb:file: -Dlunchy.db.schema=lunchydb -Dlunchy.db.driver=org.hsqldb.jdbc.JDBCDriver "-Dlunchy.picturedir=$TMPDIR" -Dhsqldb=true jetty:run

2.) Browse to http://localhost:8080 


TESTING THE PORTAL PAGE
======================= 

1.) add to /etc/hosts
```
127.0.0.1 	localhost.lunchylunch.local
127.0.0.1 	lunchylunch.local
```

2.) Browse http://lunchylunch.local:8080 to see the portal page

3.) Browse http://localhost.lunchylunch.local:8080 to see the user space created via the setup


RUNNING THE E2E TESTS
===================== 

DOCKER

1.) install docker

2.) cd docker-e2e-test && ./run.sh

LOCAL

1.) install protractor http://angular.github.io/protractor/#/ (e.g. npm -g install protractor && webdriver-manager update)

2.) run e2e-test.sh


TIPS FOR DEV
============

- install coffee-script (https://www.npmjs.org/package/coffee-script) and run `coffee -o webapp/js  -cw coffee/` from src/main

- as the latest version of maven-release-plugin is only available on the Adobe maven repository, you need: https://repo.adobe.com to release a version

- run "mvn -DgenerateDBClasses=true compile" to (re-)generate the JOOQ DB beans/proxies

- mysql and java should run in UTC. The angular FE converts to local (browser returned) time zones.

HOW TO RE-CALC THE TURN_ROUND_TIMES ON LOCATION
===============================================
update location set turn_around_time = (select ifnull(avg(travel_Time),0)+ifnull(avg(on_Site_Time),0) from reviews where reviews.fk_location=location.id);
update location set turn_around_time = null where turn_around_time = 0;

MYSQL COMPLAINS ABOUT DEFAULT VALUE FOR TIMESTAMPS?
===================================================

SET GLOBAL ...

SET SESSION ...

... sql_mode = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION'   

HOW TO EXTEND THE DB DATA MODEL
===============================

Preperation
-----------

- brew install liquibase

- Either add a user/password to all following mysql commands or put it into ~/.my.cnf

Initial setup
-------------

### Clean dev and staging schema (as there is difference in liquibase between DBs created via the maven package (`mvn -DcreateTables=true`) and standalone java program)

0.) mysql -e "drop database oli_lunchy; drop database oli_lunchy_staging;"

### Create the dev schema ('oli_lunchy')

1.) mysql -e "create database oli_lunchy"

2.) ./db-manage.sh -a

### Create the staging & prod schemas ('oli_lunchy_staging')

1.) mysql -e "create database oli_lunchy_staging"

2.) ./db-manage.sh -u

3.) Use the sql from the editor to set up the prod schema (you need to change the schema)

Changing the schema
-------------------

### Let's create a new table `foo`

1.) mysql oli_lunchy -e "CREATE TABLE foo ( id int(11) NOT NULL AUTO_INCREMENT, info varchar(255) NOT NULL, PRIMARY KEY (id) )"

### Create the liquibase xml definition

2.) ./db-manage.sh -d

### Add the liquibase xml definition to the source code

3.) Add the XML from the editor to the end of all_tables.xml. Add objectQuotingStrategy="QUOTE_ALL_OBJECTS" to all changeSets.

### Create the java classes (jooq)

4.) mvn -DgenerateDBClasses=true generate-sources

### Update the staging & prod schemas

5.) ./db-manage.sh -u

6.) Use the sql from the editor to set up the prod schema  (you need to change the schema)

### Finally update the liquibase meta data in the dev schema ('DATABASECHANGELOG')

7.) ./db-manage.sh -i
