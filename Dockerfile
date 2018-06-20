FROM openjdk:8-jdk-alpine
VOLUME /tmp

COPY target/sample-localstack-publisher.jar app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]