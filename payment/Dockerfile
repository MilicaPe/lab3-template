FROM maven:3.8.6-eclipse-temurin-17 as builder
WORKDIR /opt/payment
COPY mvnw pom.xml ./
COPY ./src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy
WORKDIR /opt/payment
EXPOSE 8080
COPY --from=builder /opt/payment/target/*.jar /opt/payment/*.jar
ENTRYPOINT ["java", "-jar", "/opt/payment/*.jar"]

