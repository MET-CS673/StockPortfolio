pipeline {
    agent any
    triggers {
        pollSCM '* * * * *' // 5 stars means poll the scm every minute
    }
    withCredentials([file(credentialsId: 'IEXCloud', variable: 'IexCloudApiKey')]) {
        // some block
    }
    tools {
        maven 'Maven 3.6.3'
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn --version'
                sh 'echo "Hello World"'
                sh 'mvn clean'
                sh 'mvn compile'
            }
        }
        stage('Test') {
            steps {
                echo 'IexCloudApiKey'
                sh 'use $IexCloudApiKey'
                sh 'mvn test'
            }
        }
        stage('Deploy') {
            steps {
                sh 'use $IexCloudApiKey'
                sh 'mvn package'
            }
        }
    }
   post {
        always {
            echo 'This will always run'
        }
        success {
            echo 'This will run only if successful'
        }
        failure {
            echo 'This will run only if failed'
        }
        unstable {
            echo 'This will run only if the run was marked as unstable'
        }
        changed {
            echo 'This will run only if the state of the Pipeline has changed'
            echo 'For example, if the Pipeline was previously failing but is now successful'
        }
    }
}