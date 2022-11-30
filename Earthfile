VERSION 0.6
FROM gradle:jdk17
RUN apt-get update && apt-get install -y \
  zip \
  && rm -rf /var/lib/apt/lists/*
ARG version=0.0.0
ARG bundle="github.com/earthly/earthly/contrib/earthfile-syntax-highlighting+export/"
COPY settings.gradle.kts build.gradle.kts ./
COPY scripts scripts
COPY src src

GET_BUNDLE:
  COMMAND
  COPY $bundle build/
  RUN scripts/bundle.sh build/earthfile-syntax-highlighting

dist:
  DO +GET_BUNDLE
  RUN sed -i 's^0.0.0^'"$version"'^g' ./build.gradle.kts
  RUN --mount=type=cache,target=/root/.gradle/caches gradle --no-daemon buildPlugin
  SAVE ARTIFACT build/distributions/earthly-intellij-plugin-$version.zip AS LOCAL earthly-intellij-plugin-$version.zip

sign:
  FROM +dist
  RUN --mount=type=cache,target=/root/.gradle/caches \
    --secret CERTIFICATE_CHAIN=+secrets/earthly-technologies/intellij-plugin/chain.crt \
    --secret PRIVATE_KEY=+secrets/earthly-technologies/intellij-plugin/private.pem \
    --secret PRIVATE_KEY_PASSWORD=+secrets/earthly-technologies/intellij-plugin/private-key-password \
    gradle --no-daemon signPlugin
  SAVE ARTIFACT build/distributions/earthly-intellij-plugin-$version.zip AS LOCAL earthly-intellij-plugin-signed-$version.zip

publish:
  FROM +sign
  RUN --push \
    --mount=type=cache,target=/root/.gradle/caches \
    --secret CERTIFICATE_CHAIN=+secrets/earthly-technologies/intellij-plugin/chain.crt \
    --secret PRIVATE_KEY=+secrets/earthly-technologies/intellij-plugin/private.pem \
    --secret PRIVATE_KEY_PASSWORD=+secrets/earthly-technologies/intellij-plugin/private-key-password \
    --secret PUBLISH_TOKEN=+secrets/earthly-technologies/intellij-plugin/marketplace-token \
    gradle --no-daemon publishPlugin

ide:
  LOCALLY
  DO +GET_BUNDLE
  RUN gradle runIde