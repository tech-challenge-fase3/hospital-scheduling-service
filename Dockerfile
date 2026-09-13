FROM eclipse-temurin:25-jdk AS build
WORKDIR /workspace
COPY hospital-parent/pom.xml hospital-parent/pom.xml
COPY hospital-scheduling-service/pom.xml hospital-scheduling-service/pom.xml
COPY hospital-scheduling-service/.mvn hospital-scheduling-service/.mvn
COPY hospital-scheduling-service/mvnw hospital-scheduling-service/mvnw
COPY hospital-scheduling-service/src hospital-scheduling-service/src
RUN chmod +x hospital-scheduling-service/mvnw \
    && ./hospital-scheduling-service/mvnw -f hospital-scheduling-service/pom.xml clean package -DskipTests

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /workspace/hospital-scheduling-service/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
