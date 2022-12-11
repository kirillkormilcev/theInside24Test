FROM amazoncorretto:17-alpine-jdk
COPY target/*.jar token.jar
ENTRYPOINT ["java","-jar","/token.jar"]