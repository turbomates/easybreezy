FROM openjdk:13-jdk as builder

RUN mkdir -p /build/gradle
WORKDIR /build
ADD gradle gradle/
ADD gradlew .
# Trigger for gradle download
RUN ./gradlew --version

ADD . .

RUN ./gradlew shadowJar --console=plain

FROM openjdk:13-slim
WORKDIR /eazybreezy
COPY --from=builder /build/build/libs/easybreezy.jar /eazybreezy
COPY _dockerfiles/logback.xml .

EXPOSE 3000

