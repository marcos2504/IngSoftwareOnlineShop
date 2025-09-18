pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-login')
        DOCKER_IMAGE = "maibarra/onlineshop"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build with Maven') {
            steps {
                sh "chmod +x mvnw"
                sh "./mvnw clean package -DskipTests -Dmodernizer.skip=true"
                archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
            }
        }

        stage('Publish with Jib') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-login',
                    usernameVariable: 'DOCKER_REGISTRY_USER',
                    passwordVariable: 'DOCKER_REGISTRY_PWD'
                )]) {
                    sh """
                        ./mvnw -ntp -Pprod compile jib:build \
                          -Djib.to.image=${DOCKER_IMAGE}:latest \
                          -Djib.to.auth.username=$DOCKER_REGISTRY_USER \
                          -Djib.to.auth.password=$DOCKER_REGISTRY_PWD
                    """
                }
            }
        }
    }
}


