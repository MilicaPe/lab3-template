FROM maven:3.8.6-eclipse-temurin-17 as builder
WORKDIR /opt/gate
COPY mvnw pom.xml ./
COPY ./src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy
WORKDIR /opt/gate
EXPOSE 8080
COPY --from=builder /opt/gate/target/*.jar /opt/gate/*.jar
ENTRYPOINT ["java", "-jar", "/opt/gate/*.jar"]

