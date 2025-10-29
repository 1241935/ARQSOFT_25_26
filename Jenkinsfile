pipeline {
  agent any

  triggers {
    githubPush()
  }

  environment {
    ENV = "${env.BRANCH_NAME}"
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
