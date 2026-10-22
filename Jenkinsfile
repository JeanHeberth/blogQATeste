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
              export PATH="/opt/homebrew/bin:/usr/local/bin:$PATH"

              echo "Procurando WAR..."
              WAR_FILE=$(find build/libs -name "*.war" | head -n 1)

              if [ -z "$WAR_FILE" ]; then
                echo "ERRO: Nenhum WAR encontrado."
                exit 1
              fi

              WAR_NAME=$(basename "$WAR_FILE")
              echo "WAR encontrado: $WAR_NAME"

              mkdir -p "$TOMCAT_WEBAPPS_UNIX"
              cp -f "$WAR_FILE" "$TOMCAT_WEBAPPS_UNIX/$WAR_NAME"

              echo "Deploy realizado com sucesso."
            '''
          } else {
            bat '''
              echo Procurando arquivo WAR...

              for %%F in ("%WORKSPACE%\\build\\libs\\*.war") do (
                echo WAR encontrado: %%~nxF
                copy /Y "%%~fF" "%TOMCAT_WEBAPPS_WINDOWS%\\%%~nxF"
                echo Deploy concluido.
                exit /b 0
              )

              echo ERRO: Nenhum WAR encontrado.
              exit /b 1
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
