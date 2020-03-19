FROM openjdk:13-jdk as builder

RUN mkdir -p /build/gradle
WORKDIR /build
ADD gradle gradle/
ADD gradlew .
# Trigger for gradle download
RUN ./gradlew --version

ADD . .

RUN ./gradlew shadowJar --console=plain

FROM node:13 as js
RUN mkdir -p /js
WORKDIR /js
ADD js .
RUN npm install && npm run build

FROM openjdk:13-slim
WORKDIR /eazybreezy
COPY --from=builder /build/build/libs/easybreezy.jar /eazybreezy
COPY --from=js /js/build /eazybreezy/js
COPY _dockerfiles/logback.xml .

VOLUME /eazybreezy/js
EXPOSE 3000

