pipeline {
    agent any

    options {
        buildDiscarder(logRotator(numToKeepStr: '15'))
        timestamps()
        timeout(time: 30, unit: 'MINUTES')
        disableConcurrentBuilds()
    }

    environment {
        REPO_URL   = 'https://github.com/ZiadHatem0/STAutoTask.git'
        BRANCH     = 'master'
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
                powershell '''
                    $chrome = "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe"
                    if (Test-Path $chrome) {
                        $version = (Get-Item $chrome).VersionInfo.FileVersion
                        Write-Host "Chrome is already installed: $version"
                    } else {
                        Write-Host "Chrome not found. Installing via Chocolatey..."
                        choco install googlechrome -y --no-progress
                        $version = (Get-Item $chrome).VersionInfo.FileVersion
                        Write-Host "Installed Chrome: $version"
                    }
                '''
            }
        }

        stage('Run Tests') {
            steps {
                bat 'mvn clean test -Dtestng.suites=testNG.xml'
            }
        }

    }

    post {
        always {
            junit testResults: 'target/surefire-reports/*.xml',
                  allowEmptyResults: true

            publishHTML(target: [
                allowMissing         : true,
                alwaysLinkToLastBuild: true,
                keepAll              : true,
                reportDir            : 'target/surefire-reports',
                reportFiles          : 'index.html',
                reportName           : 'TestNG Report'
            ])

            archiveArtifacts artifacts: 'target/surefire-reports/**',
                             allowEmptyArchive: true

            bat 'taskkill /F /IM chrome.exe /T >nul 2>&1 & exit /b 0'
            bat 'taskkill /F /IM chromedriver.exe /T >nul 2>&1 & exit /b 0'

            cleanWs()
        }

        success {
            echo 'BUILD SUCCEEDED - All tests passed.'
        }

        failure {
            echo 'BUILD FAILED - Check the TestNG Report tab for details.'
        }

        unstable {
            echo 'BUILD UNSTABLE - Some tests failed.'
        }
    }
}
