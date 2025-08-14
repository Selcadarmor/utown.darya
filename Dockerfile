# Этап сборки
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Этап запуска
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/Utown-0.0.1-SNAPSHOT.jar app.jar

CMD ["java", "-Xmx300m", "-jar", "app.jar"]


