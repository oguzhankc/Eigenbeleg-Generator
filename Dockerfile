# Stufe 1: Anwendung bauen; das Image enthält Gradle und JDK 25, der Wrapper-Download entfällt
FROM gradle:9.7.1-jdk25 AS build
WORKDIR /app

# Zuerst nur die Gradle-Dateien kopieren, damit Docker die Abhängigkeiten zwischenspeichern kann
COPY settings.gradle build.gradle ./
RUN gradle dependencies --no-daemon > /dev/null

COPY src src
RUN gradle bootJar --no-daemon

# Stufe 2: nur die fertige JAR mit der schlankeren Java-Laufzeit ausführen
FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

# Kostenloser Render-Tarif hat 512 MB Arbeitsspeicher; Java nutzt höchstens 75 % davon
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75"
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
