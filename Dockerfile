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

COPY --from=builder /builder/build/libs/creating-http-api-ktor-1.0-SNAPSHOT-all.jar /app/luxuryshopapi.jar
WORKDIR /app

CMD ["java","-jar", "luxuryshopapi.jar"]