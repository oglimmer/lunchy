LUNCHY
======

Lunchy is an application to provide lunch location information.


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

TIPS FOR DEV
============

- install coffee-script (https://www.npmjs.org/package/coffee-script)

