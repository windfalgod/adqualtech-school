FROM openjdk:11-jdk
WORKDIR /adqualtech-school

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src/ ./src/

# Build the application
RUN mvn package
COPY target/adqualtech-school.jar adqualtech-school.jar
ENTRYPOINT ["java", "-jar", "/adqualtech-school.jar"]