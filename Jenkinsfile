pipeline {
  agent any
  options {
    timestamps()
    timeout(time: 45, unit: 'MINUTES')
  }
  environment {
    // Garante locais comuns que contém utilitários esperados pelo gradle wrapper
    PATH = "${env.PATH}:/usr/local/bin:/usr/bin:/bin"
  }
  stages {
    stage('Checkout') {
      steps {
        echo "Checkout SCM"
        checkout scm
      }
    }

    stage('Prepare') {
      steps {
        echo "Verificando utilitários do sistema e permissões do gradle wrapper"
        sh '''
          set -e
          echo "PATH = $PATH"
          echo "Verificando uname e xargs..."
          if command -v uname >/dev/null 2>&1; then
            echo "uname encontrado: $(uname -a)"
          else
            echo "WARNING: uname não encontrado no PATH"
          fi
          if command -v xargs >/dev/null 2>&1; then
            echo "xargs encontrado"
          else
            echo "WARNING: xargs não encontrado no PATH"
          fi
          # Garante que o wrapper seja executável
          if [ -f "./gradlew" ]; then chmod +x ./gradlew; fi
        '''
      }
    }

    stage('Build') {
      steps {
        echo "Executando ./gradlew --no-daemon clean build"
        // Usar bash -lc para garantir compatibilidade com scripts que usam shell
        sh 'bash -lc "./gradlew --no-daemon clean build"'
      }
    }
  }

  post {
    always {
      echo "Arquivando artifacts e resultados de teste"
      archiveArtifacts artifacts: '**/build/libs/*.jar', allowEmptyArchive: true
      junit '**/build/test-results/test/*.xml'
    }
    failure {
      echo "Pipeline falhou — verifique logs acima"
    }
  }
}
