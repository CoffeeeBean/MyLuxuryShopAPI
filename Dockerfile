FROM gradle:5.4.1-jdk8-alpine as builder
USER root
WORKDIR /builder
ADD . /builder
COPY gradle.properties /builder
RUN gradle shadowJar

FROM openjdk:8-jre-alpine

RUN apk --no-cache add curl

ENV APPLICATION_USER ktor
RUN adduser -D -g '' $APPLICATION_USER

RUN mkdir /app
RUN chown -R $APPLICATION_USER /app

USER $APPLICATION_USER

COPY --from=builder /builder/build/libs/creating-http-api-ktor-1.0-SNAPSHOT-all.jar /app/my-application.jar
WORKDIR /app

CMD ["java", "-server", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-XX:InitialRAMFraction=2", "-XX:MinRAMFraction=2", "-XX:MaxRAMFraction=2", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=100", "-XX:+UseStringDeduplication", "-jar", "my-application.jar"]