# Base image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file into the container
COPY local-travel-0.0.1-SNAPSHOT.jar app.jar
COPY ./keystore.p12 /app/keystore.p12

# Expose the application port
EXPOSE 8443

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
