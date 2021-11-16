FROM maven:3.6.1-jdk-8-slim AS build
RUN mkdir -p /workspace
WORKDIR /workspace
COPY pom.xml /workspace
COPY src /workspace/src
RUN mvn -f pom.xml clean package

#Start with a base image containing Java runtime
FROM openjdk:11-slim as build

#Information around who maintains the image
MAINTAINER bankapplication

# Add the application's jar to the container
COPY target/gatewayserver-0.0.1-SNAPSHOT.jar gatewayserver-0.0.1-SNAPSHOT.jar

#execute the application
ENTRYPOINT ["java","-jar","/gatewayserver-0.0.1-SNAPSHOT.jar"]