pipeline {
    agent any

    options {
        buildDiscarder(logRotator(numToKeepStr: '15'))   // keep last 15 builds
        timestamps()                                      // prefix every log line with timestamp
        timeout(time: 30, unit: 'MINUTES')               // abort if pipeline exceeds 30 min
        disableConcurrentBuilds()                         // prevent parallel runs on same branch
    }

    environment {
        REPO_URL  = 'https://github.com/ZiadHatem0/STAutoTask.git'
        BRANCH    = 'master'
        MAVEN_OPTS = '-Xmx512m'
    }

    triggers {
        cron('0 15 * * 4')  // Thursday 3 PM
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: "${env.BRANCH}",
                    credentialsId: 'github-credentials',
                    url: "${env.REPO_URL}"
            }
        }

        stage('Setup Chrome') {
            steps {
                sh '''
                    if command -v google-chrome &>/dev/null; then
                        echo "Chrome already installed: $(google-chrome --version)"
                    else
                        echo "Installing Google Chrome..."
                        wget -q -O /tmp/chrome.deb \
                            https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
                        sudo apt-get install -y /tmp/chrome.deb
                        echo "Installed: $(google-chrome --version)"
                    fi
                '''
            }
        }

        stage('Run Tests') {
            steps {
                sh 'mvn clean test -Dtestng.suites=testNG.xml'
            }
        }

    }

    post {
        always {
            // Publish JUnit XML results (shows per-test pass/fail in Jenkins UI)
            junit testResults: 'target/surefire-reports/*.xml',
                  allowEmptyResults: true

            // Publish TestNG HTML report
            publishHTML(target: [
                allowMissing         : true,
                alwaysLinkToLastBuild: true,
                keepAll              : true,
                reportDir            : 'target/surefire-reports',
                reportFiles          : 'index.html',
                reportName           : 'TestNG Report'
            ])

            // Archive raw surefire reports as build artifacts
            archiveArtifacts artifacts: 'target/surefire-reports/**',
                             allowEmptyArchive: true

            // Clean workspace after every run to save disk space
            cleanWs()
        }

        success {
            echo "BUILD SUCCEEDED - All tests passed."
        }

        failure {
            echo "BUILD FAILED - Check the TestNG Report tab for details."
        }

        unstable {
            echo "BUILD UNSTABLE - Some tests failed."
        }
    }
}
