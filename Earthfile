VERSION 0.6
FROM gradle:7.1.0-jdk11
COPY settings.gradle.kts build.gradle.kts ./
COPY src src

test:
  RUN --mount=type=cache,target=/root/.gradle/caches gradle test --tests LexerTests

ide:
  LOCALLY
  RUN gradle runIde