FROM openjdk:17-oracle
COPY target/*.jar webgamebackend.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "webgamebackend.jar"]
