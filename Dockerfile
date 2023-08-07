FROM openjdk:11-jdk
COPY target/adqualtech-school.jar adqualtech-school.jar
ENTRYPOINT ["java", "-jar", "/adqualtech-school.jar"]