pipeline{
    agent any 
    
    stages{
        stage('get-code'){
            steps{
                git branch: 'main', url: 'https://github.com/abhipraydhoble/Project-InsureMe.git'
            }
        }
        
        stage('build-code'){
            steps{
                sh 'mvn clean package'
            }
        }
        
        stage('code-test'){
            steps{
                sh 'mvn test'
            }
        }
        stage('image-build'){
            steps{
                sh 'docker build -t tejasmurumkar/insureme:latest .'
            }    
        }
        
        stage('run-container'){
            steps{
                sh 'docker run -itd --name insureme -p 8089:8081 tejasmurumkar/insureme:latest'
            }
        }
        stage('docekerhub-push'){
            steps{
                withCredentials([usernamePassword(credentialsId: 'DockerCredentialID', passwordVariable: 'DockerPasswd', usernameVariable: 'DockerhubUsername')]) { 
                   sh "docker login -u ${env.dockerhub} -p ${env.passwd}"
                   sh 'docker push tejasmurumkar/insureme:latest'
                }
            }
        }
    }
}
