FROM maven:3.8.4-openjdk-17 as build

# Copy the project files into the container
COPY src /zalosp-api/src
COPY pom.xml /zalosp-api

# Set the working directory
WORKDIR /zalosp-api

# Build the application as a WAR file and skip tests
RUN mvn clean package -DskipTests


FROM openjdk:17-slim


# Copy WAR file to the webapps directory in Tomcat
# Ensure that the path to the WAR file matches the actual file name generated by Maven
COPY --from=build /zalosp-api/target/*.war /zalosp-api/app.war

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/zalosp-api/app.war"]