# Use official Java image
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy jar file from target folder
COPY target/*.jar app.jar

# Expose port (Render uses dynamic PORT)
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java","-jar","/app/app.jar"]