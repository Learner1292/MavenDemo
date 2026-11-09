pipeline {
    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven3'
    }

    stages {
        stage('Checkout') {
      steps {
        echo 'Downloading source code from GitHub'

        checkout scm
      }
        }

        stage('Build and Test') {
      steps {
        echo 'Building Java application and executing unit tests'

        sh 'mvn clean verify'
      }

      post {
        always {
          junit allowEmptyResults: true,
                          testResults: '**/target/surefire-reports/*.xml'
        }
      }
        }

        stage('SonarQube Analysis') {
      steps {
        echo 'Starting SonarQube code analysis'

        withSonarQubeEnv('SonarQube-Server') {
          sh '''
                    mvn org.sonarsource.scanner.maven:sonar-maven-plugin:5.1.0.4751:sonar \
                    -Dsonar.projectKey=MavenDemo \
                    -Dsonar.projectName=MavenDemo
                    '''
        }
      }
        }

        stage('Quality Gate') {
      steps {
        echo 'Waiting for SonarQube Quality Gate result'

        timeout(time: 5, unit: 'MINUTES') {
          waitForQualityGate abortPipeline: true
        }
      }
        }

        stage('Publish to Nexus') {
      steps {
        echo 'Uploading Maven artifact to Nexus'

        withCredentials([
                    usernamePassword(
                        credentialsId: 'nexus-credentials',
                        usernameVariable: 'NEXUS_USER',
                        passwordVariable: 'NEXUS_PASSWORD'
                    )
                ]) {
          sh '''
                    cat > "$WORKSPACE/jenkins-settings.xml" <<EOF
<settings>
    <servers>
        <server>
            <id>nexus-snapshots</id>
            <username>${NEXUS_USER}</username>
            <password>${NEXUS_PASSWORD}</password>
        </server>
    </servers>
</settings>
EOF

                    chmod 600 "$WORKSPACE/jenkins-settings.xml"

                    mvn -s "$WORKSPACE/jenkins-settings.xml" \
                        deploy -DskipTests
                    '''
        }
      }
        }
    }

    post {
        success {
      echo 'CI/CD Pipeline completed successfully!'
        }

        failure {
      echo 'Pipeline failed. Check the failed stage and console output.'
        }

        always {
      sh 'rm -f "$WORKSPACE/jenkins-settings.xml"'
        }
    }
}
