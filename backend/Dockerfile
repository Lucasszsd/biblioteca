FROM openjdk:21-jdk-slim
WORKDIR /app
RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*
COPY . .
RUN mvn clean package -DskipTests
RUN find target -name "*.jar" -exec cp {} app.jar \;
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]