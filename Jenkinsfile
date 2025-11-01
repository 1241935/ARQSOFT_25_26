pipeline {
  agent any

  triggers {
    githubPush()
  }

  environment {
    ENV = "${env.BRANCH_NAME}"
    DEPLOY_DIR = '"/deploy/app"'              
    JAR_NAME = 'target/psoft-g1-0.0.1-SNAPSHOT.jar'
  }

  stages {
    stage('CHECKOUT BRANCH') {
      steps {
        echo "Checking out DEV branch"
        checkout scm
      }
    }

    stage('BUILD') {
      steps {
        echo 'Building'
        sh 'mvn clean compile'
      }
    }

    stage('STATIC CODE ANALYSIS') {
      steps {
        echo 'Running Checkstyle'
        sh 'mvn checkstyle:checkstyle'
      }
    }

    stage('UNIT TESTS & COVERAGE') {
      steps {
        echo 'Running Unit Tests and Generating Coverage Report'
        sh 'mvn test jacoco:report'
      }
    }

    stage('REPORT RESULTS') {
      steps {
        echo 'Publishing reports (JUnit + Jacoco + Checkstyle + PIT)'
      }
      post {
        always {
          jacoco execPattern: '**/target/jacoco.exec'
          //recordIssues tools: [checkStyle(pattern: '**/target/checkstyle-result.xml')]
        }
      }
    }

    stage('PACKAGE') {
      steps {
        echo 'Packaging'
        sh 'mvn clean package -DskipTests'
        archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
      }
    }

    stage('LOCAL DEPLOY') {
            steps {
                echo "🚀 Deploying locally..."
        
                // 1️⃣ Criar diretório de deploy (se não existir)
                sh '''
                    mkdir -p ${DEPLOY_DIR}
                 '''

                // 2️⃣ Copiar o artefacto compilado (JAR) para esse diretório
                sh '''
                    cp ${JAR_NAME} ${DEPLOY_DIR}/
                    echo "🟢 App coppied to ${DEPLOY_DIR}"
                 '''

                // 3️⃣ Executar a aplicação em background
                sh '''
                    nohup java -jar ${DEPLOY_DIR}/ARQSOFT_25_26.jar > ${DEPLOY_DIR}/app.log 2>&1 &
                    echo "🌍 App running in http://localhost:8080"
                '''
            }
    }
  }

  post {
    success {
      echo "SUCCESS ON DEV PIPELINE!"
    }
    failure {
      echo "FAILIRE ON DEV PIPELINE."
    }
  }
}
