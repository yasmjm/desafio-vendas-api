# ETAPA 1: Compilação (Build)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copia as configurações e baixa as dependências para o cache
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o código fonte e compila gerando o .jar
COPY src ./src
RUN mvn clean package -DskipTests

# ETAPA 2: Execução (Runtime)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copia o jar gerado na etapa anterior
COPY --from=build /app/target/vendas-api-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]