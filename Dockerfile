# Build Container
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /builder
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src/ ./src/
RUN mvn clean package -DskipTests
# Layer extraction for efficiency
RUN cp target/*.jar application.jar
RUN java -Djarmode=tools -jar application.jar extract --layers --destination extracted


# Runtime Container
FROM eclipse-temurin:21
WORKDIR /application
COPY --from=builder /builder/extracted/dependencies/ ./
COPY --from=builder /builder/extracted/spring-boot-loader/ ./
COPY --from=builder /builder/extracted/snapshot-dependencies/ ./
COPY --from=builder /builder/extracted/application/ ./
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "application.jar"]
