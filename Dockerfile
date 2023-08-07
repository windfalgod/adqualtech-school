FROM openjdk:11-jdk
WORKDIR /adqualtech-school
COPY target/adqualtech-school.jar adqualtech-school.jar
ENTRYPOINT ["java", "-jar", "/adqualtech-school.jar"]