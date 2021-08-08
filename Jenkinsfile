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
//     environment {
//         IexCloudApiKey=credentials('IEXCloud')
//     }
    stages {
        stage('Test') {
            steps {
                withCredentials([file(credentialsId: 'IEXCloud', variable: 'FILE')]) {
                    dir('/Users/mlewis/.jenkins/workspace/SPD-Pipeline_master/target/classes') {
                        sh 'cat $FILE > secrets.properties'
                    }

                    dir('/Users/mlewis/.jenkins/workspace/SPD-Pipeline_master@2/target/classes') {
                        sh 'cat $FILE > secrets.properties'
                    }

                    sh 'mvn test'

                    dir('/Users/mlewis/.jenkins/workspace/SPD-Pipeline_master/target/classes') {
                        sh 'rm secrets.properties'
                    }

                    dir('/Users/mlewis/.jenkins/workspace/SPD-Pipeline_master@2/target/classes') {
                        sh 'rm secrets.properties'
                    }
                }
            }
            post { // Archive the JUnit test reports for display in the Jenkins web UI.
                success {
                    junit 'target/surefire-reports/**/*.xml'
                }
            }
        }
        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
            post {
                success {
                    archiveArtifacts 'target/*.jar'
                }
            }
        }
        stage('Deploy') {
            steps {
                echo "TODO DEPLOY TO AWS"
                //sh 'mvn package'
            }
        }
    }
   post {
        always {
            echo 'This will always run'
        }
        success {
            echo 'SUCCESS: SPD-Pipeline completed successfully'
        }
        failure {
            echo 'FAILURE: SPD-Pipeline failed'
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