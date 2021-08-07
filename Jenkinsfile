pipeline {
    agent any
    triggers {
        pollSCM '* * * * *' // 5 stars means poll the scm every minute
    }
    tools {
        maven 'Maven 3.6.3'
    }
    options {
        skipStagesAfterUnstable()
    }
    stages {
        stage('Test') {
            steps {
                script {
                    dir('/Users/mlewis/IntelliJProjects/CS673/StockPortfolio/src/main/resources') {
                        withCredentials([file(credentialsId: 'IEXCloud', variable: 'IexCloudApiKey')]) {
                            echo '$IexCloudApiKey'
                            sh 'use $IexCloudApiKey'
                            IexCloudApiKey:'${IexCloudApiKey}'
                            sh 'mvn test'
                        }
                    }
                }
            }
        }
        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
            post { // 	If the maven build succeeded, archive the JUnit test reports for display in the Jenkins web UI.
                success {
                    junit 'target/surefire-reports/**/*.xml'
                }
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