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
                    // Force a clean context session first
                    bat 'docker logout || ver > nul'

                    // This flag pattern passes the secret directly via memory parameters on Windows, avoiding space bugs entirely!
                    bat 'docker login -u %HUB_USER% -p %HUB_TOKEN%'

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