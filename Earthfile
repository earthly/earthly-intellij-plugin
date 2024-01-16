VERSION --pass-args --global-cache --use-function-keyword 0.7
PROJECT earthly-technologies/core
IMPORT github.com/earthly/lib/gradle:b4d24aed3c053db217c64c9bd7625092bb67e424 AS gradle
ARG --global gradle_version=8.2.1
ARG --global bundle="github.com/earthly/earthfile-grammar+export/"
FROM gradle:${gradle_version}-jdk17

GET_BUNDLE:
  FUNCTION
  COPY $bundle build/
  RUN scripts/bundle.sh build/earthfile-syntax-highlighting



install:
  RUN echo "aaa6"
  RUN apt-get update && apt-get install -y \
    zip \
    && rm -rf /var/lib/apt/lists/*
  COPY settings.gradle.kts build.gradle.kts ./
  # Sets $EARTHLY_GRADLE_USER_HOME_CACHE and $EARTHLY_GRADLE_PROJECT_CACHE
  DO gradle+GRADLE_GET_MOUNT_CACHE

src:
  FROM +install
  COPY src src
  COPY scripts scripts
  DO +GET_BUNDLE
  ARG --required version
  RUN sed -i 's^0.0.0^'"$version"'^g' ./build.gradle.kts

# dist builds the plugin and saves the artifact locally
dist:
  ARG version="0.0.0"
  FROM +src --version=$version
  RUN --mount=$EARTHLY_GRADLE_USER_HOME_CACHE --mount=$EARTHLY_GRADLE_PROJECT_CACHE gradle --no-daemon buildPlugin
  SAVE ARTIFACT build/distributions/earthly-intellij-plugin-$version.zip AS LOCAL earthly-intellij-plugin-$version.zip

# sign signs the plugin and saves the artifact locally
sign:
  FROM --pass-args +src
  RUN --mount=$EARTHLY_GRADLE_USER_HOME_CACHE --mount=$EARTHLY_GRADLE_PROJECT_CACHE \
    --secret CERTIFICATE_CHAIN=intellij-plugin/chain.crt \
    --secret PRIVATE_KEY=intellij-plugin/private.pem \
    --secret PRIVATE_KEY_PASSWORD=intellij-plugin/private-key-password \
    gradle --no-daemon signPlugin
  SAVE ARTIFACT build/distributions/earthly-intellij-plugin-$version.zip AS LOCAL earthly-intellij-plugin-signed-$version.zip

# publish publishes the plugin to the IntelliJ marketplace
publish:
  FROM --pass-args +src
  RUN --push \
    $EARTHLY_GRADLE_MOUNT_CACHE \
    --secret CERTIFICATE_CHAIN=intellij-plugin/chain.crt \
    --secret PRIVATE_KEY=intellij-plugin/private.pem \
    --secret PRIVATE_KEY_PASSWORD=intellij-plugin/private-key-password \
    --secret PUBLISH_TOKEN=intellij-plugin/marketplace-token \
    gradle --no-daemon publishPlugin

# generate-gradle-wrapper generates ./gradlew and its dependencies in the local machine
generate-gradle-wrapper:
  WORKDIR /tmp/wrap
  RUN gradle --no-daemon init wrapper
  RUN ls -hal
  SAVE ARTIFACT gradle AS LOCAL gradle
  SAVE ARTIFACT gradlew AS LOCAL gradlew
  SAVE ARTIFACT gradlew.bat AS LOCAL gradlew.bat

# ide opens an IntelliJ IDE with the plugin installed. Requires ./gradlew (see +generate-gradle-wrapper)
ide:
  LOCALLY
  DO +GET_BUNDLE
  RUN ./gradlew runIde
