pipeline {
  agent any

  options {
    timestamps()
    timeout(time: 45, unit: 'MINUTES')
  }

  environment {
    TOMCAT_WEBAPPS_WINDOWS = 'C:\\apache-tomcat-11.0.11\\webapps'
    TOMCAT_WEBAPPS_UNIX    = '/opt/homebrew/opt/tomcat/libexec/webapps'
  }

  stages {
    stage('Checkout') {
      steps {
        echo "Checkout SCM"
        checkout scm
      }
    }

    stage('Build') {
      steps {
        script {
          if (isUnix()) {
            echo "Executando ./gradlew --no-daemon clean build"
            sh 'chmod +x ./gradlew && ./gradlew --no-daemon clean build'
          } else {
            echo "Executando gradlew.bat --no-daemon clean build"
            bat 'gradlew.bat --no-daemon clean build'
          }
        }
      }
    }

    stage('Deploy WAR to Tomcat') {
      when {
        expression {
          def gitBranch = (env.GIT_BRANCH ?: '').toLowerCase()
          return gitBranch == 'origin/main' || gitBranch == 'origin/master'
        }
      }
      steps {
        script {
          if (isUnix()) {
            sh '''
              echo "Java do agente Jenkins:"
              java -version

              echo "Versao do Gradle:"
              ./gradlew --version

              echo "JDKs reconhecidos pelo Gradle:"
              ./gradlew -q javaToolchains
            '''
          } else {
            bat '''
              @echo off

              echo Java do agente Jenkins:
              java -version

              echo Versao do Gradle:
              call gradlew.bat --version

              echo JDKs reconhecidos pelo Gradle:
              call gradlew.bat -q javaToolchains
            '''
          }
        }
      }
    }
  }

  post {
    always {
      echo "Arquivando artifacts e resultados de teste"
      archiveArtifacts artifacts: '**/build/libs/*.jar', allowEmptyArchive: true
      junit testResults: '**/build/test-results/test/*.xml', allowEmptyResults: true
    }
    failure {
      echo "Pipeline falhou — verifique logs acima"
    }
  }
}
