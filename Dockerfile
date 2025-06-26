FROM openjdk:21

COPY fetcher-*.jar /app.jar

CMD ["java", "-jar", "/app.jar"]