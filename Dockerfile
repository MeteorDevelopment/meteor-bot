FROM eclipse-temurin:18-alpine

WORKDIR /app

COPY . .

RUN ./gradlew build

CMD [ "java", "-jar", "build/libs/meteor-bot.jar" ]
