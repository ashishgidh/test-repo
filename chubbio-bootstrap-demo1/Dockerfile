FROM nexus.chubbdigital.com:8099/chubb-rhel8-jre17:v8.7-1026-2022q4-j17.0.2

USER root

RUN mkdir /app

ARG JAR_DIR=target/quarkus-app
COPY ${JAR_DIR} /app/quarkus-app

COPY applicationinsights-agent-3.4.7.jar /app/
COPY applicationinsights.json /app/

# RUN mkdir /var/log/logger
# EXPOSE 8080
ENTRYPOINT ["java","-javaagent:/app/applicationinsights-agent-3.4.7.jar",  "-Dlog4j2.formatMsgNoLookups=true","-jar","/app/quarkus-app/quarkus-run.jar"]
