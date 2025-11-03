pipeline {
    agent any

    environment {
        CODECOV_TOKEN = credentials('CODECOV')
        GITHUB_TOKEN = credentials('GITHUB_TOKEN')
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
                    jacoco(
                        execPattern: '**/jacocoTest.exec',
                        classPattern: '**/classes',
                        sourcePattern: '**/src/main/java',
                        inclusionPattern: '**/*.class'
                    )
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
                        bat 'curl -s https://codecov.io/bash | bash -s -- -t %CODECOV_TOKEN%'
                    }
                }
            }
        }

        // =========================================================
        // 7️⃣ DEPLOY TO GITHUB PAGES (somente na main)
        // =========================================================
        stage('Publish Jacoco to GitHub Pages') {
            when {
                branch 'main'
            }
            steps {
                script {
                    echo "🚀 Publicando relatório Jacoco no GitHub Pages..."
                    if (isUnix()) {
                        sh '''
                            git config --global user.email "ci@jenkins.local"
                            git config --global user.name "Jenkins CI"
                            REPO_URL="https://x-access-token:${GITHUB_TOKEN}@github.com/${GITHUB_REPOSITORY}.git"
                            git clone --branch gh-pages ${REPO_URL} gh-pages || git clone ${REPO_URL} gh-pages
                            mkdir -p gh-pages/jacoco
                            cp -R build/reports/jacoco/test/html/* gh-pages/jacoco/
                            cd gh-pages
                            git add .
                            git commit -m "Atualiza relatório Jacoco [ci skip]" || echo "Nenhuma alteração detectada"
                            git push ${REPO_URL} gh-pages
                        '''
                    } else {
                        bat '''
                            git config --global user.email "ci@jenkins.local"
                            git config --global user.name "Jenkins CI"
                            set REPO_URL=https://x-access-token:%GITHUB_TOKEN%@github.com/%GITHUB_REPOSITORY%.git
                            git clone --branch gh-pages %REPO_URL% gh-pages || git clone %REPO_URL% gh-pages
                            mkdir gh-pages\\jacoco
                            xcopy /E /I build\\reports\\jacoco\\test\\html gh-pages\\jacoco
                            cd gh-pages
                            git add .
                            git commit -m "Atualiza relatório Jacoco [ci skip]" || echo "Nenhuma alteração detectada"
                            git push %REPO_URL% gh-pages
                        '''
                    }
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
