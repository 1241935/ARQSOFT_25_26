pipeline {
  agent any

  environment {
    HOME_DIR = "${env.HOME}"
    SERVER = 'vsgate-ssh.dei.isep.ipp.pt'
    PORT = '11001'
    DEPLOY_PATH_DEV = "${env.HOME}/bms_app/dev"
    DEPLOY_PATH_STAGING = '/bms_app/staging'
    DEPLOY_PATH_PROD = '/bms_app/prod'
    JAR_NAME = 'psoft-g1-0.0.1-SNAPSHOT.jar'
    SSH_CREDENTIALS = 'dei-ssh'
  }

  stages {
    // ❌ REMOVER - multibranch faz checkout automático
    // stage('CHECKOUT BRANCH') { ... }

    stage('BUILD') {
      steps {
        sh 'mvn clean compile'
      }
    }

    stage('STATIC CODE ANALYSIS') {
      steps {
        sh 'mvn checkstyle:checkstyle'
      }
    }

    stage('UNIT TESTS & COVERAGE') {
      steps {
        sh 'mvn test jacoco:report'
      }
      post {
        always {
          junit '**/target/surefire-reports/*.xml'
          jacoco execPattern: '**/target/jacoco.exec'
        }
      }
    }

    stage('MUTATION TESTS') {
      steps {
        sh 'mvn org.pitest:pitest-maven:mutationCoverage'
      }
    }

    stage('PACKAGE') {
      steps {
        sh 'mvn package -DskipTests'
        archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
      }
    }

    stage('DEPLOY DEV') {
      when { branch 'dev' }  // ← ADICIONAR
      steps {
        echo "🚀 Deploying to DEV (local)"
        sh """
          mkdir -p ${DEPLOY_PATH_DEV}
          cp target/${JAR_NAME} ${DEPLOY_PATH_DEV}/
          pkill -f 'spring.profiles.active=dev' || true
          sleep 2
          nohup java -jar ${DEPLOY_PATH_DEV}/${JAR_NAME} --spring.profiles.active=dev \\
            > ${DEPLOY_PATH_DEV}/app.log 2>&1 < /dev/null &
        """
      }
    }

    stage('DEPLOY STAGING') {
      when { branch 'staging' }  // ← ADICIONAR
      steps {
        echo "🚀 Deploying to STAGING (${SERVER})"
        sshagent(credentials: [env.SSH_CREDENTIALS]) {
          sh """
            scp -o StrictHostKeyChecking=no -P ${PORT} target/${JAR_NAME} root@${SERVER}:${DEPLOY_PATH_STAGING}/
            ssh -o StrictHostKeyChecking=no -p ${PORT} root@${SERVER} '
              pkill -f "spring.profiles.active=staging" || true
              sleep 2
              nohup java -jar ${DEPLOY_PATH_STAGING}/${JAR_NAME} \\
                --spring.profiles.active=staging \\
                --server.port=2224 \\
                > ${DEPLOY_PATH_STAGING}/app.log 2>&1 < /dev/null &
            ' || true
          """
        }
      }
    }

    stage('DEPLOY PROD') {
      when { branch 'main' }  // ← ADICIONAR
      steps {
        echo "🚀 Deploying to PROD (${SERVER})"
        sshagent(credentials: [env.SSH_CREDENTIALS]) {
          sh """
            scp -o StrictHostKeyChecking=no -P ${PORT} target/${JAR_NAME} root@${SERVER}:${DEPLOY_PATH_PROD}/
            ssh -o StrictHostKeyChecking=no -p ${PORT} root@${SERVER} '
              pkill -f "spring.profiles.active=prod" || true
              sleep 2
              nohup java -jar ${DEPLOY_PATH_PROD}/${JAR_NAME} \\
                --spring.profiles.active=prod \\
                --server.port=2225 \\
                > ${DEPLOY_PATH_PROD}/app.log 2>&1 < /dev/null &
            ' || true
          """
        }
      }
    }

    stage('HEALTH CHECK') {
      steps {
        script {
          sleep 10

          if (env.BRANCH_NAME == 'dev') {
            sh """
              pgrep -f 'spring.profiles.active=dev' && echo '✅ DEV is running' || echo '⚠️ DEV not detected'
            """
          } else if (env.BRANCH_NAME == 'staging') {
            sshagent(credentials: [env.SSH_CREDENTIALS]) {
              sh """
                ssh -o StrictHostKeyChecking=no -p ${PORT} root@${SERVER} 'pgrep -f "spring.profiles.active=staging"' \\
                  && echo '✅ STAGING is running' || echo '⚠️ STAGING not detected'
              """
            }
          } else if (env.BRANCH_NAME == 'main') {
            sshagent(credentials: [env.SSH_CREDENTIALS]) {
              sh """
                echo "Checking PROD..."
                ssh -o StrictHostKeyChecking=no -p ${PORT} root@${SERVER} 'pgrep -f "spring.profiles.active=prod"' \\
                  && echo '✅ PROD is running' || echo '⚠️ PROD not detected'
              """
            }
          }
        }
      }
    }
  }

  post {
    success {
      echo "✅ PIPELINE SUCCESS on branch ${env.BRANCH_NAME}!"
    }
    failure {
      echo "❌ PIPELINE FAILED on branch ${env.BRANCH_NAME}."
    }
  }
}