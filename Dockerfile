# Use official Java image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy jar file from target folder
COPY target/*.jar app.jar

# Expose port (Render uses dynamic PORT)
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java","-jar","/app/app.jar"]