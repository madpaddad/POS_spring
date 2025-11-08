# window
# FROM openjdk:11

#linux
# FROM openjdk:21-slim
FROM eclipse-temurin:21-jdk
WORKDIR /demo
CMD ["./gradlew", "clean", "bootJar"]
COPY build/libs/*.jar app.jar

#EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]