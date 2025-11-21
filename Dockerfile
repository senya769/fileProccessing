FROM openjdk:17-ea-5-alpine
WORKDIR /app
COPY target/demo-0.0.1-SNAPSHOT.jar /app/filesystem.jar
ENTRYPOINT ["java","-jar","filesystem.jar"]