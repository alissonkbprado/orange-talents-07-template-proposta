### Builder Image
#FROM maven:3.6.3-jdk-11 AS builder
#COPY src /usr/src/app/src
#COPY pom.xml /usr/src/app
#RUN mvn -f /usr/src/app/ clean package
#
### Runner Image
#FROM openjdk:11
#COPY --from=builder /usr/src/app/target/*.jar /usr/app/app.jar
#EXPOSE 8080
#ENTRYPOINT ["java","-Xmx512m","-jar","/usr/app/app.jar"]

FROM openjdk:11
#RUN addgroup -S spring && adduser -S spring -G spring
#USER spring:spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Xmx512m","-Dserver.port=8080","-jar","/app.jar"]
EXPOSE 8080


### Builder Image
#FROM maven:3.6.3-jdk-11 AS builder
#COPY src /usr/src/app/src
#COPY pom.xml /usr/src/app
#RUN mvn -f /usr/src/app/pom.xml clean package