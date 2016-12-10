#!/bin/bash

git clone https://github.com/oglimmer/lunchy

# by default the main space is reachable under localhost, but we need it at webe2e
cp base_data.xml lunchy/src/main/resources

cd lunchy

DB_USER=SA
DB_URL=jdbc:hsqldb:file:
DB_SCHEMA=lunchydb
DB_DRIVER=org.hsqldb.jdbc.JDBCDriver
DB_PARAM="-Dlunchy.db.user=$DB_USER -Dlunchy.db.url=$DB_URL -Dlunchy.db.schema=$DB_SCHEMA -Dlunchy.db.driver=$DB_DRIVER"

mvn $DB_PARAM -DcreateTables=true process-resources 

mvn $DB_PARAM "-Dlunchy.picturedir=$TMPDIR" -Dhsqldb=true jetty:run
