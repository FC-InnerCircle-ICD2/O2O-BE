FROM eclipse-temurin:21-jdk-alpine

EXPOSE 8082

COPY ./build/libs/application-admin-1.0-SNAPSHOT.jar admin-app.jar

ENTRYPOINT ["java", "-jar", "-Duser.timezone=Asia/Seoul", "/admin-app.jar", "--spring.profiles.active=dev"]
