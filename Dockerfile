FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/*.jar rekrutacja-1.0.0.jar
ENTRYPOINT ["java","-jar","/rekrutacja-1.0.0.jar"]