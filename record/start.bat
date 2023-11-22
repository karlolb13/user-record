@echo off

rem Build the Maven project
call mvn clean compile install

rem Check if the Maven build was successful
if %ERRORLEVEL% NEQ 0 (
    echo Maven build failed, exiting...
    exit /b %ERRORLEVEL%
)

rem Build the Docker image
call docker build -t my-app .

rem Check if the Docker build was successful
if %ERRORLEVEL% NEQ 0 (
    echo Docker build failed, exiting...
    exit /b %ERRORLEVEL%
)

rem Run the Docker container
docker run -p 8080:8080 my-app