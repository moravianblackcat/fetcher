FROM openjdk:21

COPY build/libs/fetcher-*.jar /app.jar

CMD ["java", "-jar", "/app.jar"]