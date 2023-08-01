FROM webhippie/temurin:18

WORKDIR /app

COPY gradle ./gradle
COPY src ./src
COPY build.gradle .
COPY gradlew .
COPY settings.gradle .

RUN ./gradlew build

ENTRYPOINT java $JAVA_OPTS -jar build/libs/meteor-bot-all.jar
