FROM eclipse-temurin:21-jdk-alpine

EXPOSE 8080

COPY ./build/libs/*.jar client-app.jar

ENTRYPOINT ["java", "-jar","/client-app.jar"]
