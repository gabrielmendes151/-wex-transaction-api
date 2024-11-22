# Use uma imagem do JDK como base
FROM openjdk:17-jdk-slim

# Define o diretório de trabalho no container
WORKDIR /app

# Copie os arquivos do projeto para o container
COPY . .

# Construa o projeto usando Gradle
RUN ./gradlew build -x test

# Exponha a porta padrão do Spring Boot
EXPOSE 8080

# Execute o .jar gerado
CMD ["java", "-jar", "build/libs/wex-transaction-api-0.0.1-SNAPSHOT.jar"]
