FROM maven:3.8.6-eclipse-temurin-17 as builder
WORKDIR /opt/loyalty
COPY mvnw pom.xml ./
COPY ./src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy
WORKDIR /opt/loyalty
EXPOSE 8080
COPY --from=builder /opt/loyalty/target/*.jar /opt/loyalty/*.jar
ENTRYPOINT ["java", "-jar", "/opt/loyalty/*.jar"]

