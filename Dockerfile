FROM openjdk:17-oracle
CMD ["./gradlew", "clean", "build"]
VOLUME /tmp
ARG JAR_FILE=build/libs/alle-api-server-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} alle.jar
ENTRYPOINT ["java", "-jar", "/alle.jar"]