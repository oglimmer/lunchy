#!/bin/sh

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
	mvn -Dlunchy.db.user=SA -Dlunchy.db.url=jdbc:hsqldb:file: -Dlunchy.db.schema=lunchydb -Dlunchy.db.driver=org.hsqldb.jdbc.JDBCDriver -DcreateTables=true process-resources 
} &>e2e-test-logs/createdb.log

# compile and start jetty
{
	mvn -Dlunchy.db.user=SA -Dlunchy.db.url=jdbc:hsqldb:file: -Dlunchy.db.schema=lunchydb -Dlunchy.db.driver=org.hsqldb.jdbc.JDBCDriver "-Dlunchy.picturedir=$TMPDIR" -Dhsqldb=true jetty:run &
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
protractor protractor-conf.js
cd ../../..

# stop webdriver and jetty
kill $WEBDRIVER_PID
kill $JETTY_PID

# clear database files
rm -rf lunchydb.*
