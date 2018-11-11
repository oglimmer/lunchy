module.exports = {

  config: {
    SchemaVersion: "1.0.0",
    Name: "Lunchy",
    Vagrant: {
      Box: 'ubuntu/xenial64',
      Install: 'maven openjdk-8-jdk-headless mysql-client-5.7 docker.io nodejs'
    },
    JavaVersions: [ "1.8" ]
  },

  software: {

    lunchy: {
      Source: "mvn",
      Artifact: "target/lunchy##001.war",
    },

    mysql: {
      Source: "mysql",
      Mysql: {
        Schema: "oli_lunchy"
      },
      configFile: {
        Name: "my.cnf",
        Content: [
          "[mysqld]",
          "collation-server = utf8_unicode_ci",
          "init-connect='SET NAMES utf8'",
          "character-set-server = utf8",
          "sql-mode=\"ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION\""
        ],
        AttachIntoDocker: "/etc/mysql/conf.d" 
      },
      AfterStart: [
        "mvn -DcreateTables=true process-resources"
      ]
    },

    tomcat: {
      Source: "tomcat",
      Deploy: "lunchy"
    }
  }

}