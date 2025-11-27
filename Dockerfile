FROM maven:3.9.11-ibm-semeru-17-noble AS builder

WORKDIR /gateway

COPY . .

RUN mvn clean package -DskipTests

FROM tomcat:jdk25
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=builder /gateway/target/gateway.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD [ "catalina.sh", "run" ]