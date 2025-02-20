FROM eclipse-temurin:21-jdk-alpine

EXPOSE 8085

COPY ./build/libs/application-oss-1.0-SNAPSHOT.jar oss-app.jar

ENTRYPOINT ["java", "-jar", "-Duser.timezone=Asia/Seoul", "/oss-app.jar", "--spring.profiles.active=dev"]
