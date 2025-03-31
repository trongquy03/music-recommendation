FROM openjdk:17

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} music-web.jar

ENTRYPOINT ["java", "-jar", "backend-service.jar"]

EXPOSE 8080