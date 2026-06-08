pipeline {
    agent any

    tools {
        jdk 'JDK17'
        maven 'Maven'
    }

    triggers {
        cron('0 15 * * 4')  // Thursday 3 PM
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Install Chrome') {
            steps {
                sh '''
                    if ! command -v google-chrome &> /dev/null; then
                        wget -q -O /tmp/chrome.deb https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
                        apt-get install -y /tmp/chrome.deb || true
                    fi
                    google-chrome --version
                '''
            }
        }

        stage('Run Tests') {
            steps {
                sh 'mvn test -Dtestng.suites=testNG.xml'
            }
        }
    }

    post {
        always {
            junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true

            publishHTML(target: [
                allowMissing         : true,
                alwaysLinkToLastBuild: true,
                keepAll              : true,
                reportDir            : 'target/surefire-reports',
                reportFiles          : 'index.html',
                reportName           : 'TestNG Report'
            ])
        }

        success {
            echo 'All tests passed.'
        }

        failure {
            echo 'Some tests failed. Check the TestNG report for details.'
        }
    }
}
