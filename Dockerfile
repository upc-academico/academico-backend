# Etapa 1: Build
FROM maven:3.9.11-eclipse-temurin-17 AS build
WORKDIR /app

# Usar la cache de las dependencias, solo se debe ejecutar de nuevo si el pom.xml cambia
COPY pom.xml .
RUN mvn dependency:go-offline -B \
    -Dmaven.wagon.http.pool=false \
    -Dmaven.wagon.httpconnectionManager.ttlSeconds=120

# Compilar proyecto
COPY src ./src
RUN mvn clean package -DskipTests \
    -Dmaven.wagon.http.pool=false

# Etapa 2: Runtime
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Variables de entorno
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC"

# Copiar JAR desde etapa de build
COPY --from=build /app/target/jwt-demo-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
