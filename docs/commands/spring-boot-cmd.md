
1. Command to run the application (via Maven):

    mvn spring-boot:run

2. Running a JAR file directly:

   java -jar target/your-application.jar

3. Creating an Executable JAR/WAR

   Maven:
   mvn clean package
   
3. Testing a Spring Boot Application
   Running unit tests with Maven:

   mvn test
   
4. Running Spring Boot Application with Profiles
   Using a specific profile:
   
   mvn spring-boot:run -Dspring-boot.run.profiles=dev

   Or if running from the JAR:
   java -jar target/your-application.jar --spring.profiles.active=dev

7. Spring Boot Actuator Commands
   Accessing Actuator endpoints: After adding spring-boot-starter-actuator to your dependencies, you can access various endpoints like health, info, etc.

   Health check endpoint: http://localhost:8080/actuator/health
   Info endpoint: http://localhost:8080/actuator/info

9. Running with Docker
   Build Docker image for Spring Boot:
   
   docker build -t my-spring-boot-app .
   
10. Running Docker container:

   docker run -p 8080:8080 my-spring-boot-app

10. Spring Boot Profiles in Different Environments
    You can set different profiles for different environments like dev, prod, test, etc. Spring Boot automatically loads the application-{profile}.properties or .yml based on the active profile.
    properties
    Copy code
    spring.profiles.active=dev

11. Spring Boot Logging Configuration
    Changing logging level in application.properties:
   
    logging.level.org.springframework=INFO
    logging.level.com.yourapp=DEBUG

12. Spring Boot Remote Debugging
    Enable remote debugging (JVM):
   
    java -jar target/your-application.jar -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000
    Then, connect to the remote application using an IDE.

17. Profile-Specific Logging Configuration
    Example of logging configuration for different profiles:
    properties
    Copy code
    logging.level.com.example=DEBUG
    logging.level.com.example.production=ERROR