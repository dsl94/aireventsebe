FROM openjdk:17
MAINTAINER nemanja
EXPOSE 8080
ARG JAR_FILE=target/airevents-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} api.jar
ENTRYPOINT ["java", "-jar", "-Xms512M","-Xmx512M", "api.jar"]
