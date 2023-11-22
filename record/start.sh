#!/bin/bash

# Build the Maven project
mvn clean compile install

# Build the Docker image
docker build -t my-app .

#Run the docker image
docker run -p 8080:8080 my-app