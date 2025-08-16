# Базовый образ с Java 21
FROM eclipse-temurin:24-jdk

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файлы проекта в контейнер
COPY . .

# Делаем Maven Wrapper исполняемым (на случай, если права не скопировались)
RUN chmod +x mvnw

# Собираем приложение (пропускаем тесты для ускорения)
RUN ./mvnw clean package -DskipTests

# Запускаем Spring Boot приложение
# PORT приходит от Railway, передаем его в Spring через переменную
EXPOSE 8080
CMD ["sh", "-c", "java -jar target/*.jar --server.port=${PORT:-8080}"]