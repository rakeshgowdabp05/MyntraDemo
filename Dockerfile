FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B clean package -DskipTests

FROM tomcat:10.1-jdk21-temurin
WORKDIR /usr/local/tomcat

RUN rm -rf webapps/*

COPY --from=build /app/target/MyntraDemo.war webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
