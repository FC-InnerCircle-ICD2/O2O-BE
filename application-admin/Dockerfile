FROM eclipse-temurin:21-jdk-alpine

EXPOSE 8081

COPY ./build/libs/*.jar admin-app.jar

ENTRYPOINT ["java", "-jar","/admin-app.jar"]
