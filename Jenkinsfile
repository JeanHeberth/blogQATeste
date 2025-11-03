pipeline {
    agent any

    tools {
        jdk 'JDK21'
    }

    environment {
        JAVA_HOME = "C:\\Users\\maq_mac\\.jdks\\corretto-21.0.8"
        PATH = "${env.JAVA_HOME}\\bin;${env.PATH}"
        CODECOV_TOKEN = credentials('CODECOV')
        GITHUB_TOKEN = credentials('GITHUB_TOKEN')
        // Força Gradle a usar o mesmo Java que o Jenkins
        ORG_GRADLE_JAVA_HOME = "${env.JAVA_HOME}"
    }

    stages {

            // =========================================================
            // 1️⃣ CHECKOUT
            // =========================================================
            stage('Checkout') {
                steps {
                    echo "🔄 Clonando o repositório..."
                    checkout scm
                }
            }

            // =========================================================
            // 2️⃣ BUILD
            // =========================================================
            stage('Build') {
                steps {
                    script {
                        echo "⚙️ Executando build do projeto..."
                        if (isUnix()) {
                            sh './gradlew clean build -x test'
                        } else {
                            bat 'gradlew clean build -x test'
                        }
                    }
                }
            }

            // =========================================================
            // 3️⃣ UNIT TESTS - SERVICE
            // =========================================================
            stage('Unit Tests - Service') {
                steps {
                    script {
                        echo "🧪 Executando testes unitários da camada Service..."
                        if (isUnix()) {
                            sh './gradlew test --tests "br.com.blogqateste.service.*"'
                        } else {
                            bat 'gradlew test --tests "br.com.blogqateste.service.*"'
                        }
                    }
                }
                post {
                    always {
                        junit '**/build/test-results/test/TEST-*.xml'
                    }
                }
            }

            // =========================================================
            // 4️⃣ INTEGRATION TESTS
            // =========================================================
            stage('Integration Tests') {
                steps {
                    script {
                        echo "🔗 Executando testes de integração..."
                        if (isUnix()) {
                            sh './gradlew test --tests "br.com.blogqateste.integration.*"'
                        } else {
                            bat 'gradlew test --tests "br.com.blogqateste.integration.*"'
                        }
                    }
                }
                post {
                    always {
                        junit '**/build/test-results/test/TEST-*.xml'
                    }
                }
            }

            // =========================================================
            // 5️⃣ REPORTS & COVERAGE
            // =========================================================
            stage('Reports & Coverage') {
                steps {
                    script {
                        echo "📊 Gerando relatórios de cobertura Jacoco..."
                        if (isUnix()) {
                            sh './gradlew jacocoTestReport'
                        } else {
                            bat 'gradlew jacocoTestReport'
                        }
                    }
                }
                post {
                    always {
                        junit '**/build/test-results/test/TEST-*.xml'
                        publishHTML(target: [
                            reportDir: 'build/reports/jacoco/test/html',
                            reportFiles: 'index.html',
                            reportName: 'Jacoco Coverage Report'
                        ])
                    }
                }
            }

            // =========================================================
            // 6️⃣ UPLOAD TO CODECOV
            // =========================================================
            stage('Upload Coverage to Codecov') {
                steps {
                    script {
                        echo "☁️ Enviando relatório de cobertura para Codecov..."
                        if (isUnix()) {
                            sh 'curl -s https://codecov.io/bash | bash -s -- -t ${CODECOV_TOKEN}'
                        } else {
                            bat 'codecov.exe -t %CODECOV_TOKEN%'
                        }
                    }
                }
            }

            // =========================================================
            // 7️⃣ DEPLOY TO TOMCAT (Windows)
            // =========================================================
            stage('Deploy to Tomcat') {
                when {
                    branch 'main'
                }
                steps {
                    script {
                        echo "🚀 Iniciando deploy automático no Tomcat 11..."
                        if (isUnix()) {
                            sh './scripts/deploy_tomcat.sh'
                        } else {
                            bat 'powershell -ExecutionPolicy Bypass -File deploy_tomcat.ps1'
                        }
                    }
                }
            }


        // =========================================================
        // 🔄 POST ACTIONS (sempre executadas)
        // =========================================================
        post {
            always {
                echo '✅ Pipeline concluído.'
            }
            success {
                echo '🎉 Todos os stages executados com sucesso!'
            }

            failure {
                echo '❌ Falha detectada no pipeline. Verifique os logs.'
            }
        }
    }
