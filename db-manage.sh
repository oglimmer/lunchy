#!/bin/bash

STAGING=oli_lunchy_staging
DEV=oli_lunchy
DIFF_FILE=/tmp/liquibase_diff.xml
UPDATE_FILE=/tmp/liquibase_diff.sql
CLASSPATH=$HOME/.m2/repository/mysql/mysql-connector-java/5.1.38/mysql-connector-java-5.1.38.jar

usage="$(basename "$0") [-d] [-u] [-i] [-c] [-a] - liquibase wrapper

where:
    -h  shows this help text
    -d  diffs dev to staging
    -u  updates staging
    -i  updates dev
    -a  init dev
    -c  clear checksum on dev and staging"

cd ${0%/*}

while getopts ':hduica' option; do
  case "$option" in
    h) echo "$usage"
       exit
       ;;
    d) DIFF=YES
       ;;
    u) UPDATE_STAG=YES
       ;;
    i) UPDATE_DEV=YES
       ;;
    c) CLEAR_CHECKSUM=YES
	   ;;
	a) INIT_DEV=YES
	   ;;
    :) printf "missing argument for -%s\n" "$OPTARG" >&2
       echo "$usage" >&2
       exit 1
       ;;
   \?) printf "illegal option: -%s\n" "$OPTARG" >&2
       echo "$usage" >&2
       exit 1
       ;;
  esac
done
shift $((OPTIND - 1))


if [ -n "$DIFF" ]; then

	echo "Diff $DEV against $STAGING to $DIFF_FILE"
	
	rm -f $DIFF_FILE
		
	liquibase --driver=com.mysql.jdbc.Driver --classpath=$CLASSPATH --changeLogFile=$DIFF_FILE --url=jdbc:mysql://127.0.0.1/$STAGING?useSSL=false --username=root --referenceUrl=jdbc:mysql://127.0.0.1/$DEV?useSSL=false --referenceUsername=root diffChangeLog 
	
	echo "$DIFF_FILE created. Will now be opened in editor. Copy it to your change file repository" 
	
	subl $DIFF_FILE

fi


if [ -n "$UPDATE_STAG" ]; then

	
	echo "Applying changes to $STAGING to $UPDATE_FILE"
	
	liquibase --driver=com.mysql.jdbc.Driver --classpath=$CLASSPATH --changeLogFile=./src/main/resources/all_tables.xml --url=jdbc:mysql://127.0.0.1/$STAGING?useSSL=false --username=root updateSQL >$UPDATE_FILE
	
	mysql -uroot -e "source $UPDATE_FILE"
	
	echo "$STAGING was updated. Change SQL file will be opened in editor. Execute it on your production database."
	
	subl $UPDATE_FILE	 
fi 


if [ -n "$UPDATE_DEV" ]; then

	
	echo "Applying changes to $DEV"
	
	liquibase --driver=com.mysql.jdbc.Driver --classpath=$CLASSPATH --changeLogFile=./src/main/resources/all_tables.xml --url=jdbc:mysql://127.0.0.1/$DEV?useSSL=false --username=root updateSQL | grep DATABASECHANGELOG | while read line ; do
   		mysql -uroot -e "$line"
	done
		 
	echo "$DEV.DATABASECHANGELOG was updated (no DDL was executed)." 
fi 

if [ -n "$INIT_DEV" ]; then
	
	echo "Init $DEV"
	
	liquibase --driver=com.mysql.jdbc.Driver --classpath=$CLASSPATH --changeLogFile=./src/main/resources/all_tables.xml --url=jdbc:mysql://127.0.0.1/$DEV?useSSL=false --username=root updateSQL >$UPDATE_FILE
		 
	mysql -uroot -e "source $UPDATE_FILE"

	echo "$DEV.DATABASECHANGELOG was updated (schema created)." 
fi 


if [ -n "$CLEAR_CHECKSUM" ]; then

	
	echo "Clearing checksums on DEV and STAGING"
	
	liquibase --driver=com.mysql.jdbc.Driver --classpath=$CLASSPATH --changeLogFile=./src/main/resources/all_tables.xml --url=jdbc:mysql://127.0.0.1/$DEV?useSSL=false --username=root clearCheckSums
	liquibase --driver=com.mysql.jdbc.Driver --classpath=$CLASSPATH --changeLogFile=./src/main/resources/all_tables.xml --url=jdbc:mysql://127.0.0.1/$STAGING?useSSL=false --username=root clearCheckSums
		 
	echo "Done." 
fi 
