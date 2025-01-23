FROM eclipse-temurin:21-jdk-alpine

EXPOSE 8081

COPY ./build/libs/application-client-1.0-SNAPSHOT.jar client-app.jar

ENTRYPOINT ["java", "-jar", "/client-app.jar", "--spring.profiles.active=dev"]
