FROM openjdk:11-jdk
RUN docker compose up
RUN mvn clean package
RUN docker compose down
COPY target/adqualtech-school.jar adqualtech-school.jar
ENTRYPOINT ["java", "-jar", "/adqualtech-school.jar"]