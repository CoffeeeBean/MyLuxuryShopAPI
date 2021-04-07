FROM gradle:5.4.1-jdk8-alpine as builder
USER root
WORKDIR /builder
ADD . /builder
COPY gradle.properties /builder
RUN gradle build --no-daemon --stacktrace

FROM openjdk:8-jre-alpine
WORKDIR /app
COPY --from=builder /builder/build/libs/creating-http-api-ktor-1.0-SNAPSHOT.jar .
ENTRYPOINT ["java", "-jar", "creating-http-api-ktor-1.0-SNAPSHOT.jar"]