#!/bin/sh

DB_USER=SA
DB_URL=jdbc:hsqldb:file:
DB_SCHEMA=lunchydb
DB_DRIVER=org.hsqldb.jdbc.JDBCDriver
DB_PARAM="-Dlunchy.db.user=$DB_USER -Dlunchy.db.url=$DB_URL -Dlunchy.db.schema=$DB_SCHEMA -Dlunchy.db.driver=$DB_DRIVER"

rm -rf e2e-test-logs
mkdir e2e-test-logs

# start webdriver (with chrome-driver)
{
	webdriver-manager start &
} &>e2e-test-logs/webdriver.log

# clear database files
rm -rf lunchydb.*

# create new database (file based)
{
	mvn $DB_PARAM -DcreateTables=true process-resources 
} &>e2e-test-logs/createdb.log

# compile and start jetty
{
	mvn $DB_PARAM "-Dlunchy.picturedir=$TMPDIR" -Dhsqldb=true jetty:run &
} &>e2e-test-logs/jetty.log

# wait for jetty to be started
WEBDRIVER_PID=$(lsof -t -i @0.0.0.0:4444 -sTCP:listen)
echo "WEBDRIVER_PID:"$WEBDRIVER_PID

while [ -z "$JETTY_PID" ]
do
	sleep 3
	JETTY_PID=$(lsof -t -i @0.0.0.0:8080 -sTCP:listen)
done
echo "JETTY_PID:"$JETTY_PID
sleep 1

# execute e2e-test
cd src/integration/js
protractor protractor.conf.js
cd ../../..

# stop webdriver and jetty
kill $WEBDRIVER_PID
kill $JETTY_PID

# clear database files
rm -rf lunchydb.*
