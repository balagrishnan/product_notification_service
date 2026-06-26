pipeline {
    agent any

    environment {
        REGISTRY_CREDENTIALS_ID = 'docker-hub-credentials' // Name of your Jenkins credentials ID
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
                script {
                    echo "Building Docker Image: ${IMAGE_NAME}:${IMAGE_TAG}..."
                    // Notice no build-args or ports are compiled here
                    dockerImage = docker.build("${IMAGE_NAME}:${IMAGE_TAG}")
                }
            }
        }

        /*stage('Login & Push to Registry') {
            steps {
                script {
                    // Authenticate and securely push the background image to your repository
                    docker.withRegistry('', REGISTRY_CREDENTIALS_ID) {
                        dockerImage.push()
                        dockerImage.push('latest')
                    }
                }
            }
        }*/

        stage('Login & Push to Registry') {
            steps {
                // We change the variable mappings to HUB_USER and HUB_TOKEN
                withCredentials([usernamePassword(credentialsId: "${REGISTRY_CREDENTIALS_ID}",
                                                 usernameVariable: 'HUB_USER',
                                                 passwordVariable: 'HUB_TOKEN')]) {

                    // Force a clean session state on the host first
                    sh 'docker logout || true'

                    // Log in using our freshly named variables wrapped securely in single quotes
                    sh 'echo "$HUB_TOKEN" | docker login -u "$HUB_USER" --password-stdin'

                    // Push your builds to Docker Hub
                    sh "docker push ${IMAGE_NAME}:${IMAGE_TAG}"
                    sh "docker push ${IMAGE_NAME}:latest"
                }
            }
        }

        stage('Deploy Container') {
            steps {
                script {
                    echo "Deploying background worker service..."
                    // Wipe any existing container instance with the same name
                    sh "docker rm -f product-notification-service || true"

                    // Run the container without the "-p" port mapping flag!
                    // Attached to 'product-network' to communicate with your product-infra broker
                    sh """
                        docker run -d \
                        --name product-notification-service \
                        --network product-network \
                        --restart always \
                        ${IMAGE_NAME}:latest
                    """
                }
            }
        }
    }

    post {
        success {
            echo "Successfully built and deployed product-notification-service Worker!"
        }
        failure {
            echo "Pipeline failed. Check Jenkins console outputs."
        }
    }
}