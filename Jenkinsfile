pipeline {
  agent any

  tools{

    go 'go-1.17'

  }

  stages {
    stage('Test'){

        steps{

            git ''

        }

    }
  }


  stages {
    stage('Build') {
      steps {
        echo 'Building...'
      }
    }
    stage('Test') {
      steps {
        echo 'Testing...'
      }
    }
    stage('Deploy') {
      steps {
        echo 'Deploying...'
      }
    }
  }
}