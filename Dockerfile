FROM registryrfsc.azurecr.io/base/alpine/openjdk17:0.0.1 AS builder

COPY . /app
WORKDIR /app
RUN ./gradlew clean build --info --stacktrace --no-daemon

FROM registryrfsc.azurecr.io/base/alpine/openjdk17:0.0.1

COPY --from=builder /app/build/libs/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-server", "-jar", "/app/app.jar"]
