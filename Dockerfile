# Use an official JDK image with Maven preinstalled
FROM maven:3.8.7-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Build the application
RUN mvn clean package

# Use a minimal JDK image to run the app
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy the built JAR file from the previous step
COPY --from=build /app/target/*.jar app.jar

# Expose the application port (change if necessary)
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]
