pipeline {
    agent any

    environment {
        TOMCAT_WEBAPPS_WINDOWS = 'C:\\apache-tomcat-11.0.11\\webapps'
        TOMCAT_WEBAPPS_UNIX    = '/opt/homebrew/opt/tomcat/libexec/webapps'
        WAR_NAME               = 'blogQATeste.war'
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Clonando repositório...'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                script {
                    if (isUnix()) {
                        sh '''
                            export JAVA_HOME=$(/usr/libexec/java_home -v 21)
                            export PATH="$JAVA_HOME/bin:/opt/homebrew/bin:/usr/local/bin:$PATH"

                            chmod +x gradlew
                            ./gradlew clean build -x test
                        '''
                    } else {
                        bat 'gradlew clean build -x test'
                    }
                }
            }
        }

        stage('Deploy WAR to Tomcat') {
            when {
                expression {
                    // GIT_BRANCH vem como 'origin/main' em job comum e BRANCH_NAME como 'main' em multibranch
                    def branch = (env.BRANCH_NAME ?: env.GIT_BRANCH ?: '').toLowerCase().replaceFirst('^origin/', '')
                    echo "Branch atual: ${branch}"
                    return branch in ['main', 'master']
                }
            }

            steps {
                script {
                    if (isUnix()) {
                        sh '''
                            export PATH="/opt/homebrew/bin:/usr/local/bin:$PATH"

                            WAR_FILE="build/libs/$WAR_NAME"

                            if [ ! -f "$WAR_FILE" ]; then
                                echo "ERRO: $WAR_FILE não encontrado. Conteúdo de build/libs:"
                                ls -la build/libs
                                exit 1
                            fi

                            mkdir -p "$TOMCAT_WEBAPPS_UNIX"
                            cp -f "$WAR_FILE" "$TOMCAT_WEBAPPS_UNIX/$WAR_NAME"

                            echo "Deploy realizado com sucesso."
                        '''
                    } else {
                        bat '''
                            if not exist "%WORKSPACE%\\build\\libs\\%WAR_NAME%" (
                                echo ERRO: %WAR_NAME% nao encontrado.
                                exit /b 1
                            )

                            copy /Y "%WORKSPACE%\\build\\libs\\%WAR_NAME%" "%TOMCAT_WEBAPPS_WINDOWS%\\%WAR_NAME%"
                            echo Deploy concluido.
                        '''
                    }
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline concluído.'
        }

        success {
            echo 'Build e Deploy executados com sucesso!'
        }

        failure {
            echo 'Falha detectada no pipeline.'
        }
    }
}
