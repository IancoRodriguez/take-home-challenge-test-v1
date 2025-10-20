FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/take-home-challenge-test-0.0.1.jar
COPY ${JAR_FILE} take-home-challenge.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar" , "take-home-challenge.jar"]