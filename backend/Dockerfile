# Use the OpenJDK base image to build the application
FROM openjdk:21-jdk AS build

# Set the working directory
WORKDIR /app

# Copy the Maven wrapper and pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Ensure the mvnw script is executable
RUN chmod +x mvnw

# Download dependencies (this will cache dependencies unless pom.xml changes)
RUN ./mvnw dependency:go-offline

# Copy the rest of the application
COPY src ./src

# Package the application
RUN ./mvnw package -DskipTests

# Use a smaller JRE image to run the application
FROM openjdk:21-jdk

# Copy the jar file from the build stage
COPY --from=build /app/target/jobjet-1.0.0.jar /app/jobjet-1.0.0.jar

# Expose port 8443
EXPOSE 8443

# Command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/jobjet-1.0.0.jar"]