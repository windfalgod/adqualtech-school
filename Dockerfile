FROM openjdk:11-jdk
RUN docker start adqualtech-school-phpmyadmin-1
RUN docker start adqualtech-school-local-mysql-1
RUN mvn clean package \
Run docker compose down
COPY target/adqualtech-school.jar adqualtech-school.jar
ENTRYPOINT ["java", "-jar", "/adqualtech-school.jar"]