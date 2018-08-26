#!/usr/bin/env bash

trap cleanup 2
set -e


#------------
# FunctionsBuilder
#------------



	jdk_version() {
		
  # returns the JDK version.
  # 8 for 1.8.0_nn, 9 for 9-ea etc, and "no_java" for undetected
  # from https://stackoverflow.com/questions/7334754/correct-way-to-check-java-version-from-bash-script
  local result
  local java_cmd
  if [[ -n $(type -p java) ]]
  then
    java_cmd=java
  elif [[ (-n "$JAVA_HOME") && (-x "$JAVA_HOME/bin/java") ]]
  then
    java_cmd="$JAVA_HOME/bin/java"
  fi
  local IFS=$'\n'
  # remove \r for Cygwin
  local lines=$("$java_cmd" -Xms32M -Xmx32M -version 2>&1 | tr '\r' '\n')
  if [[ -z $java_cmd ]]
  then
    result=no_java
  else
    for line in $lines; do
      if [[ (-z $result) && ($line = *"version \""*) ]]
      then
        local ver=$(echo $line | sed -e 's/.*version "\(.*\)"\(.*\)/\1/; 1q')
        # on macOS, sed doesn't support '?'
        if [[ $ver = "1."* ]]
        then
          result=$(echo $ver | sed -e 's/1\.\([0-9]*\)\(.*\)/\1/; 1q')
        else
          result=$(echo $ver | sed -e 's/\([0-9]*\)\(.*\)/\1/; 1q')
        fi
      fi
    done
  fi
  echo "$result"

	}





#------------
# CleanupBuilder
#------------


cleanup()
{
  echo "****************************************************************"
  echo "Stopping software .....please wait...."
  echo "****************************************************************"

  ALL_COMPONENTS=(mysql tomcat)
  for keepRunningAllElement in "${ALL_COMPONENTS[@]}"; do
    IFS=',' read -r -a array <<< "$KEEP_RUNNING"
    found=0
    for keepRunningToFindeElement in "${array[@]}"; do
      if [ "$keepRunningAllElement" == "$keepRunningToFindeElement" ]; then
        echo "Not stopping $keepRunningAllElement!"
        found=1
      fi
    done
    if [ "$found" -eq 0 ]; then

      if [ "$keepRunningAllElement" == "mysql" ]; then
        echo "Stopping $keepRunningAllElement ..."
        if [ "$TYPE_SOURCE_MYSQL" == "docker" ]; then
         docker rm -f $dockerContainerIDmysql
         rm -f .mysql
        fi
        
      fi
      if [ "$keepRunningAllElement" == "tomcat" ]; then
        echo "Stopping $keepRunningAllElement ..."
        if [ "$TYPE_SOURCE_TOMCAT" == "docker" ]; then
         docker rm -f $dockerContainerIDtomcat
         rm -f .tomcat
        fi
        if [ "$TYPE_SOURCE_TOMCAT" == "download" ]; then
         ./localrun/apache-tomcat-$TOMCAT_VERSION/bin/shutdown.sh
         rm -f .tomcat
        fi
        
      fi
    fi
  done

  exit 0
}




#------------
# OptionsBuilder
#------------



usage="$(basename "$0") - Builds, deploys and run Lunchy
where:
  -h                         show this help text
  -s                         skip any build
  -c [all|build]             clean local run directory, when a build is scheduled for execution it also does a full build
  -k [component]             keep comma sperarated list of components running
  -t [component:type:[path|version]] run component inside [docker] container, [download] component (default) or [local] use installed component from path
  -v                         start VirtualBox via vagrant, install all dependencies, ssh into the VM and run
  -b local|docker:version    build locally (default) or within a maven image on docker, the default image is 3-jdk-10
  -f                         tail the apache catalina log at the end

Details:
 -b docker:[3-jdk-8|3-jdk-9|3-jdk-10] #do a docker based build, uses \`maven:3-jdk-10\` image
 -b local #do a local build, would respect -j
 -t mysql:local #reuse a local, running MySQL installation, does not start/stop this MySQL
 -t mysql:docker:[5|8] #start docker image \`mysql:X\`
 -t tomcat:local:/usr/lib/tomcat #reuse tomcat installation from /usr/lib/tomcat, does not start/stop this tomcat
 -t tomcat:download:[7|8|9] #download tomcat version x and run this build within it, would respect -j
 -t tomcat:docker:[7|8|9] #start docker image \`tomcat:X\` and run this build within it
"

cd $(cd "$(dirname "$0")";pwd -P)

BUILD=local
while getopts ':hsc:k:t:vb:f' option; do
  case "$option" in
    h) echo "$usage"
       exit;;
    s) SKIP_BUILD=YES;;
    c) 
       CLEAN=$OPTARG
       if [ "$CLEAN" != "all" -a "$CLEAN" != "build" ]; then
         echo "Illegal -c parameter" && exit 1
       fi
       ;;
    k) KEEP_RUNNING=$OPTARG;;
    t) TYPE_SOURCE=$OPTARG;;
    v) VAGRANT=YES;;
    b) BUILD=$OPTARG;;
    f) TAIL=YES;;
    :) printf "missing argument for -%s\n" "$OPTARG" >&2
       echo "$usage" >&2
       exit 1;;
   \?) printf "illegal option: -%s\n" "$OPTARG" >&2
       echo "$usage" >&2
       exit 1;;
  esac
done
shift $((OPTIND - 1))
TYPE_PARAM="$1"




#------------
# DependencycheckBuilder
#------------

mvn --version 1>/dev/null || exit 1; 
docker --version 1>/dev/null || exit 1; 
mysql --version 1>/dev/null || exit 1; 
curl --version 1>/dev/null || exit 1; 
java -version 2>/dev/null || exit 1; 




#------------
# CleanBuilder
#------------


# clean if requested
if [ -n "$CLEAN" ]; then
  if [ "$CLEAN" == "all" ]; then
    rm -rf localrun
  fi
  
fi




#------------
# GlobalVariablesBuilder
#------------

TYPE_SOURCE_MYSQL=docker
TYPE_SOURCE_TOMCAT=download



#------------
# PrepareBuilder
#------------


mkdir -p localrun



if [ "$VAGRANT" == "YES" -a "$VAGRANT_IGNORE" != "YES" ]; then
  mkdir -p localrun
  cd localrun
  cat <<-EOF > Vagrantfile
# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure("2") do |config|
  config.vm.box = "ubuntu/xenial64"
  config.vm.network "forwarded_port", guest: 8080, host: 8080
  config.vm.synced_folder "../", "/share_host"
  config.vm.provider "virtualbox" do |vb|
    vb.memory = "1024"
  end
  config.vm.provision "shell", inline: <<-SHELL
    debconf-set-selections <<< 'mysql-server mysql-server/root_password password ""'
    debconf-set-selections <<< 'mysql-server mysql-server/root_password_again password ""'
    ln -s /usr/bin/nodejs /usr/bin/node
    npm install -g phantomjs-prebuilt
    apt-get update
    apt-get install -y maven openjdk-8-jdk-headless mysql-server-5.7 docker.io npm
    
    echo "Now continue with..."
    echo "\$ cd /share_host"
    echo "\$ ./run_local.sh -f"
    echo "...then browse to http://localhost:8080/XXXX"
  SHELL
end
EOF
  vagrant up
  if [ -f "../run_local.sh" ]; then
    vagrant ssh -c "cd /share_host && ./run_local.sh -f"
  else
    echo "Save the fulgens output into a bash script (e.g. run_local.sh) and use it inside the new VM"
  fi
  exit 1
fi


if [ "$(uname)" == "Darwin" ]; then export JAVA_HOME=$(/usr/libexec/java_home -v 1.8); fi



#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
# JavaPlugin
#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


#------------
# PrepareCompBuilder
#------------





#------------
# GetsourceBuilder
#------------





#------------
# PrebuildBuilder
#------------





#------------
# BuildBuilder
#------------





#------------
# PostbuildBuilder
#------------





#------------
# PrestartBuilder
#------------





#------------
# StartBuilder
#------------





#------------
# PoststartBuilder
#------------





#------------
# LeaveCompBuilder
#------------




#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
# MvnPlugin
#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


#------------
# PrepareCompBuilder
#------------





#------------
# GetsourceBuilder
#------------





#------------
# PrebuildBuilder
#------------





#------------
# BuildBuilder
#------------



if [ "$BUILD" == "local" ]; then
  f_build() {
    
    mvn $MVN_CLEAN $MVN_OPTS package
    
  }
elif [[ "$BUILD" == docker* ]]; then
  IFS=: read mainType dockerVersion <<< "$BUILD"
  if [ -z "$dockerVersion" ]; then
    dockerVersion=3-jdk-10
  fi

  f_build() {
    
    docker run --rm -v "$(pwd)":/usr/src/build -v "$(pwd)/localrun/.m2":/root/.m2 -w /usr/src/build maven:$dockerVersion mvn $MVN_CLEAN $MVN_OPTS package
    
  }
    
fi   
if [ "$SKIP_BUILD" != "YES" ]; then
  if [ -n "$CLEAN" ]; then
    MVN_CLEAN=clean
  fi
  f_build
fi





#------------
# PostbuildBuilder
#------------





#------------
# PrestartBuilder
#------------





#------------
# StartBuilder
#------------





#------------
# PoststartBuilder
#------------





#------------
# LeaveCompBuilder
#------------





#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
# MysqlPlugin
#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


#------------
# PrepareCompBuilder
#------------



IFS=',' read -r -a array <<< "$TYPE_SOURCE"
for typeSourceElement in "${array[@]}"; do
  IFS=: read comp type pathOrVersion <<< "$typeSourceElement"
  if [ "$comp" == "mysql" ]; then
    TYPE_SOURCE_MYSQL=$type
    if [ "$TYPE_SOURCE_MYSQL" == "local" ]; then
      TYPE_SOURCE_MYSQL_PATH=$pathOrVersion
    else
      TYPE_SOURCE_MYSQL_VERSION=$pathOrVersion
    fi
  fi

done

if [ "$TYPE_SOURCE_MYSQL" == "docker" ]; then
  if [ -z "$TYPE_SOURCE_MYSQL_VERSION" ]; then
    TYPE_SOURCE_MYSQL_VERSION=5
  fi
  
fi




#------------
# GetsourceBuilder
#------------





#------------
# PrebuildBuilder
#------------





#------------
# BuildBuilder
#------------





#------------
# PostbuildBuilder
#------------





#------------
# PrestartBuilder
#------------





#------------
# StartBuilder
#------------



if [ "$TYPE_SOURCE_MYSQL" == "docker" ]; then
  # run in docker
  if [ ! -f ".mysql" ]; then
    
    mkdir -p localrun/a85d01fa
    cat <<EOT > localrun/a85d01fa/my.cnf
[mysqld]
collation-server = utf8_unicode_ci
init-connect='SET NAMES utf8'
character-set-server = utf8
sql-mode="ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION"
EOT
      
    dockerContainerIDmysql=$(docker run --rm -d -p 3306:3306 -e MYSQL_ALLOW_EMPTY_PASSWORD=true \
      -v $(pwd)/localrun/a85d01fa:/etc/mysql/conf.d mysql:$TYPE_SOURCE_MYSQL_VERSION)
    echo "$dockerContainerIDmysql">.mysql
  else
    dockerContainerIDmysql=$(<.mysql)
  fi
fi
if [ "$TYPE_SOURCE_MYSQL" == "local" ]; then
  if [ -f .mysql ]; then
    echo "mysql running but started from different source type"
    exit 1
  fi
fi





#------------
# PoststartBuilder
#------------



while ! mysql -uroot --protocol=tcp -e "select 1" 1>/dev/null 2>&1; do
  echo "waiting for mysql..."
  sleep 3
done

mysql -uroot --protocol=tcp -NB -e "create database if not exists oli_lunchy"

mvn -DcreateTables=true process-resources




#------------
# LeaveCompBuilder
#------------





#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
# TomcatPlugin
#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


#------------
# PrepareCompBuilder
#------------



IFS=',' read -r -a array <<< "$TYPE_SOURCE"
for typeSourceElement in "${array[@]}"; do
  IFS=: read comp type pathOrVersion <<< "$typeSourceElement"
  if [ "$comp" == "tomcat" ]; then
    TYPE_SOURCE_TOMCAT=$type
    if [ "$TYPE_SOURCE_TOMCAT" == "local" ]; then
      TYPE_SOURCE_TOMCAT_PATH=$pathOrVersion
    else
      TYPE_SOURCE_TOMCAT_VERSION=$pathOrVersion
    fi
  fi

done

if [ "$TYPE_SOURCE_TOMCAT" == "docker" ]; then
  if [ -z "$TYPE_SOURCE_TOMCAT_VERSION" ]; then
    TYPE_SOURCE_TOMCAT_VERSION=9
  fi
  
fi

if [ "$TYPE_SOURCE_TOMCAT" == "download" ]; then
  if [ -z "$TYPE_SOURCE_TOMCAT_VERSION" ]; then
    TYPE_SOURCE_TOMCAT_VERSION=9
  fi
  # find latest tomcat version for $TYPE_SOURCE_TOMCAT_VERSION
  if [ "$(uname)" == "Linux" ]; then
    GREP_PERL_MODE="-P"
  fi
  TOMCAT_BASE_URL="http://mirror.vorboss.net/apache/tomcat"
  TOMCAT_VERSION_PRE=$(curl -s "$TOMCAT_BASE_URL/tomcat-$TYPE_SOURCE_TOMCAT_VERSION/"|grep -m1 -o $GREP_PERL_MODE "<a href=\"v\d*.\d*.\d*" || echo "__________9.0.10")
  TOMCAT_VERSION=${TOMCAT_VERSION_PRE:10}
  TOMCAT_URL=$TOMCAT_BASE_URL/tomcat-$TYPE_SOURCE_TOMCAT_VERSION/v$TOMCAT_VERSION/bin/apache-tomcat-$TOMCAT_VERSION.tar.gz
fi




#------------
# GetsourceBuilder
#------------



if [ "$TYPE_SOURCE_TOMCAT" == "download" ]; then
  if [ -f .tomcat ] && [ "$(<.tomcat)" != "download" ]; then
    echo "Tomcat running but started from different source type"
    exit 1
  fi
  # download tomcat
  if [ ! -f "/${TMPDIR:-/tmp}/apache-tomcat-$TOMCAT_VERSION.tar" ]; then
    curl -s $TOMCAT_URL | gzip -d >/${TMPDIR:-/tmp}/apache-tomcat-$TOMCAT_VERSION.tar
  fi
  # extract tomcat
  if [ ! -d "./apache-tomcat-$TOMCAT_VERSION" ]; then
    tar -xf /${TMPDIR:-/tmp}/apache-tomcat-$TOMCAT_VERSION.tar -C ./localrun
  fi
fi





#------------
# PrebuildBuilder
#------------





#------------
# BuildBuilder
#------------





#------------
# PostbuildBuilder
#------------





#------------
# PrestartBuilder
#------------





#------------
# StartBuilder
#------------



if [ "$TYPE_SOURCE_TOMCAT" == "docker" ]; then
  if [ -f .tomcat ] && [ "$(<.tomcat)" == "download" ]; then
    echo "Tomcat running but started from different source type"
    exit 1
  fi
  if [ ! -f ".tomcat" ]; then
    
    dockerContainerIDtomcat=$(docker run --rm -d $dockerCouchRef ${dockerFixRef[@]} -p 8080:8080  -v "$(pwd)/localrun/webapps":/usr/local/tomcat/webapps tomcat:$TYPE_SOURCE_TOMCAT_VERSION)
    echo "$dockerContainerIDtomcat">.tomcat
  else
    dockerContainerIDtomcat=$(<.tomcat)
  fi
  tailCmd="docker logs -f $dockerContainerIDtomcat"
fi



if [ "$TYPE_SOURCE_TOMCAT" == "download" ]; then
  # start tomcat
  if [ ! -f ".tomcat" ]; then
    
    ./localrun/apache-tomcat-$TOMCAT_VERSION/bin/startup.sh
    echo "download">.tomcat
  fi
  tailCmd="tail -f ./localrun/apache-tomcat-$TOMCAT_VERSION/logs/catalina.out"
fi



if [ "$TYPE_SOURCE_TOMCAT" == "local" ]; then
  if [ -f .tomcat ]; then
    echo "Tomcat running but started from different source type"
    exit 1
  fi
  tailCmd="tail -f $TYPE_SOURCE_TOMCAT_PATH/logs/catalina.out"
fi





#------------
# PoststartBuilder
#------------



    
            if [ "$TYPE_SOURCE_TOMCAT" == "docker" ]; then
              mkdir -p localrun/webapps
              targetPath=localrun/webapps/
            fi
          

            if [ "$TYPE_SOURCE_TOMCAT" == "download" ]; then
              targetPath=localrun/apache-tomcat-$TOMCAT_VERSION/webapps/
            fi
          

            if [ "$TYPE_SOURCE_TOMCAT" == "local" ]; then
              targetPath=$TYPE_SOURCE_TOMCAT_PATH/webapps/
            fi
          
    f_deploy() {
      cp target/lunchy##001.war $targetPath
    }
    f_deploy
    




#------------
# LeaveCompBuilder
#------------





#------------
# WaitBuilder
#------------


# waiting for ctrl-c
if [ "$TAIL" == "YES" ]; then
  $tailCmd
else
  echo "$tailCmd"
  echo "<return> to rebuild, ctrl-c to stop MySQL, Tomcat"
  while true; do
    read </dev/tty
    f_build
    f_deploy
  done
fi
    


