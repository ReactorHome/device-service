FROM java:8
COPY /target/device-service-0.0.1-SNAPSHOT.jar /device-service-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT java -jar /device-service-0.0.1-SNAPSHOT.jar