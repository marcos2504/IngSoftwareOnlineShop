#!/usr/bin/env groovy

pipeline {
    agent {
        docker {
            image 'jhipster/jhipster:v8.11.0'
            args '-u jhipster -e MAVEN_OPTS="-Duser.home=./"'
        }
    }

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

        stage('Check Java') {
            steps {
                sh "java -version"
            }
        }

        stage('Clean') {
            steps {
                sh "chmod +x mvnw"
                sh "./mvnw -ntp clean -P-webapp"
            }
        }

        stage('Install Tools') {
            steps {
                sh "./mvnw -ntp com.github.eirslett:frontend-maven-plugin:install-node-and-npm@install-node-and-npm"
            }
        }

        stage('NPM Install') {
            steps {
                sh "./mvnw -ntp com.github.eirslett:frontend-maven-plugin:npm"
            }
        }

        stage('Package App') {
            steps {
                sh "./mvnw clean package -DskipTests -Dmodernizer.skip=true"
                archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
            }
        }

        stage('Build & Push Docker Image') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', DOCKERHUB_CREDENTIALS) {
                        def app = docker.build(DOCKER_IMAGE + ":latest")
                        app.push()
                    }
                }
            }
        }
    }
}

