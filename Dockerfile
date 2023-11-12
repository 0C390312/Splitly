FROM eclipse-temurin:21.0.1_12-jre-ubi9-minimal

COPY ./build/libs/splitly-core-0.0.1-SNAPSHOT.jar /splitly-core.jar

EXPOSE 8080

CMD ["java", "-jar", "/splitly-core.jar"]