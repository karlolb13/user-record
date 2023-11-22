FROM adoptopenjdk/openjdk11:alpine-jre
#VOLUME /tmp
#ARG JAR_FILE=target/*.jar
ARG JAR_FILE=./web/target/*-exec.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

#docker build -t my-app .
#docker run -d -p 8080:8080 my-app

#FROM adoptopenjdk/openjdk11:alpine-jre
#WORKDIR /app
#COPY --from=build /app/web/target/*-exec.jar app.jar
#ENTRYPOINT ["java","-jar","/app/app.jar"]