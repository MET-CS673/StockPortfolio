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
    environment {
        BRANCH_NAME = "${GIT_BRANCH.split('/').size() > 1 ? GIT_BRANCH.split('/')[1..-1].join('/') : GIT_BRANCH}"
    }
    stages {
        stage('Unit Test') {
            steps {
                    sh 'mvn test'
            }
            post { // 	If the maven goal succeeded, archive the JUnit test reports for display in the Jenkins web UI.
                success {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Integration Test') {
            steps {
                withCredentials([file(credentialsId: 'IEXCloud', variable: 'FILE')]) {
                    dir('/Users/mlewis/.jenkins/workspace/SPD-Pipeline_' + BRANCH_NAME + '/target/classes') {
                        sh 'cat $FILE > secrets.properties'
                        echo "${BRANCH_NAME}"
                    }

                    sh 'mvn -Dmaven.clean.skip=true failsafe:integration-test'
                }
            }
        }
        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
            post { // 	If the maven build succeeded, archive the jar file
                success {
                    archiveArtifacts 'target/*.jar'
                }
            }
        }
        stage('Deploy') {
            steps {
                echo "TODO DEPLOY TO AWS"
                //sh 'mvn -DskipTests deploy'
            }
        }
    }
   post {
        always {
            echo 'Cleaning up resources...'
            echo 'Removing secrets.properties files from Jenkins directories'
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