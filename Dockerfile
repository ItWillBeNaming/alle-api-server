FROM openjdk:17-oracle
CMD ["./gradlew", "clean", "build"]
VOLUME /tmp
ARG JAR_FILE=build/libs/alle.jar
COPY ${JAR_FILE} alle.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "/alle.jar"]