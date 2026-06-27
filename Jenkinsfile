pipeline {
    agent any

    environment {
        REGISTRY_CREDENTIALS_ID = 'docker-hub-credentials'
        IMAGE_NAME = 'your-dockerhub-username/product-notification-service'
        IMAGE_TAG = "${BUILD_NUMBER}"
    }

    stages {
        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }

        stage('Build Docker Image') {
            steps {
                echo "Building Docker Image..."
                // Changed 'sh' to 'bat' for Windows execution
                bat "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
                bat "docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${IMAGE_NAME}:latest"
            }
        }

        stage('Login & Push to Registry') {
            steps {
                withCredentials([usernamePassword(credentialsId: "${REGISTRY_CREDENTIALS_ID}",
                                                 usernameVariable: 'HUB_USER',
                                                 passwordVariable: 'HUB_TOKEN')]) {
                    // Changed 'sh' to 'bat' and modified syntax for Windows CMD
                    bat 'docker logout || ver > nul'
                    bat 'echo %HUB_TOKEN% | docker login -u %HUB_USER% --password-stdin'
                    bat "docker push ${IMAGE_NAME}:${IMAGE_TAG}"
                    bat "docker push ${IMAGE_NAME}:latest"
                }
            }
        }

        stage('Deploy Container') {
            steps {
                echo "Deploying background worker service..."
                // Stop and remove container gracefully on Windows CMD
                bat "docker rm -f product-notification-service || ver > nul"
                bat """
                    docker run -d ^
                    --name product-notification-service ^
                    --network product-network ^
                    --restart always ^
                    ${IMAGE_NAME}:latest
                """
            }
        }
    }

    post {
        always {
            bat "docker logout || ver > nul"
        }
        success {
            echo "Successfully built and deployed product-notification-service!"
        }
    }
}