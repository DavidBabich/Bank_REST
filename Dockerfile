FROM openjdk:17-jdk-slim

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN apt-get update && apt-get install -y maven
RUN mvn clean package -DskipTests
RUN addgroup --system javauser && adduser --system --ingroup javauser javauser
COPY target/*.jar app.jar
RUN chown -R javauser:javauser /app
USER javauser
EXPOSE 8080

# Запуск приложения
ENTRYPOINT ["java", "-jar", "app.jar"] 