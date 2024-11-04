FROM maven:3.8.6-eclipse-temurin-17 as builder
WORKDIR /opt/reservation
COPY mvnw pom.xml ./
COPY ./src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy
WORKDIR /opt/reservation
EXPOSE 8080
COPY --from=builder /opt/reservation/target/*.jar /opt/reservation/*.jar
ENTRYPOINT ["java", "-jar", "/opt/reservation/*.jar"]

