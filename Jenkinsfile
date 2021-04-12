node {
    checkout scm

    docker.withRegistry('https://index.docker.io/', 'dockerHub') {

        def customImage = docker.build("luxuryshopapi:${env.BUILD_ID}")

        /* Push the container to the custom Registry */
        customImage.push()
    }
}