FROM eclipse-temurin:17-jdk-alpine
COPY power_plant.jar app.jar
EXPOSE 8080

EXPOSE 587

ENTRYPOINT ["java","-jar","/app.jar"]