# Backend Dockerfile

FROM openjdk:17-jdk-slim as build
WORKDIR /app

COPY gradlew gradlew.bat settings.gradle.kts build.gradle.kts ./
COPY gradle ./gradle

RUN ./gradlew dependencies --no-daemon
COPY src ./src

RUN ./gradlew build --no-daemon -x test

FROM openjdk:17-jdk-slim
WORKDIR /app

RUN apt-get update && apt-get install -y tzdata

ENV TZ=Europe/Paris
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
