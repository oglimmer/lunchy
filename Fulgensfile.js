module.exports = {

  config: {
    SchemaVersion: "1.0.0",
    Name: "Lunchy",
    Vagrant: {
      Box: 'ubuntu/xenial64',
      Install: 'maven openjdk-8-jdk-headless mysql-client-5.7 docker.io nodejs'
    }
  },

  versions: {
    lunchy: {
      Docker: "3-jdk-8",
      JavaLocal: "1.8",
      KnownMax: "Java 1.8"
    },
    mysql: {
      Docker: "5",
      KnownMax: "Mysql 5.x"
    },
    tomcat: {
      Docker: "tomcat9-openjdk8-openj9", // tomcat:7-jre8
      TestedWith: "7 & 9. Java 8."
    }
  },

  software: {

    lunchy: {
      Source: "mvn",
      Artifact: "target/lunchy##001.war",
      config: {
        Name: "lunchy.properties",
        Content: [
          { Line: "{" },
          { Line: "\"db.url\": \"\"" },
          { Line: "}" }
        ],
        Connections: [{
          Source: "mysql",
          Regexp: "\"db.url\": \"\"",
          Line: "\"db.url\": \"jdbc:mysql://$$VALUE$$/\"",
        }],
        AttachAsEnvVar: ["JAVA_OPTS", "-Dlunchy.properties=$$SELF_NAME$$"]
      }
    },

    mysql: {
      Source: "mysql",
      DockerMemory: "150M",
      Mysql: {
        Schema: "oli_lunchy"
      },
      configFile: {
        Name: "my.cnf",
        Content: [
          { Line: "[mysqld]" },
          { Line: "collation-server = utf8_unicode_ci" },
          { Line: "init-connect='SET NAMES utf8'" },
          { Line: "character-set-server = utf8" },
          { Line: "sql-mode=\"ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION\" " }
        ],
        AttachIntoDocker: "/etc/mysql/conf.d" 
      },
      AfterStart: [
        "mvn -DcreateTables=true process-resources"
      ]
    },

    tomcat: {
      Source: "tomcat",
      DockerImage: "oglimmer/adoptopenjdk-tomcat",
      DockerMemory: "150M",
      Deploy: "lunchy",
      EnvVars: [
        { Name: "JAVA_OPTS", Value: "-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap"}
      ]
    }
  }

}
