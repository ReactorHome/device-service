FROM openjdk:8-jre
MAINTAINER Reactor <reactor@myreactorhome.com>

ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/share/device-service/device-service.jar"]

# Add the service itself
ARG JAR_FILE
ADD ./target/${JAR_FILE} /usr/share/device-service/device-service.jar