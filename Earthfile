VERSION --pass-args --global-cache 0.7
IMPORT github.com/earthly/lib/gradle:dbf7cbb9115c02809ebd7cea217bee955ddde6d8 AS gradle
ARG --global gradle_version=8.2.1
ARG --global bundle="github.com/earthly/earthfile-grammar+export/"

install:
  FROM gradle:${gradle_version}-jdk17
  RUN apt-get update && apt-get install -y \
    zip \
    && rm -rf /var/lib/apt/lists/*
  COPY settings.gradle.kts build.gradle.kts ./
  DO gradle+INIT

src:
  FROM +install
  COPY src src
  COPY scripts scripts
  COPY $bundle build/
  RUN scripts/bundle.sh build/earthfile-syntax-highlighting

dist:
  FROM +src
  ARG version=0.0.0
  RUN sed -i 's^0.0.0^'"$version"'^g' ./build.gradle.kts
  DO gradle+RUN_WITH_CACHE --command="gradle --configuration-cache --no-daemon buildPlugin"
  SAVE ARTIFACT build/distributions/earthly-intellij-plugin-$version.zip AS LOCAL earthly-intellij-plugin-$version.zip

sign:
  ARG --required version
  FROM --pass-args +dist
  RUN --mount=type=cache,id=gradle_cache,target=/root/.gradle/caches \
    --secret CERTIFICATE_CHAIN=+secrets/earthly-technologies/intellij-plugin/chain.crt \
    --secret PRIVATE_KEY=+secrets/earthly-technologies/intellij-plugin/private.pem \
    --secret PRIVATE_KEY_PASSWORD=+secrets/earthly-technologies/intellij-plugin/private-key-password \
  DO gradle+RUN_WITH_CACHE --command="gradle --no-daemon signPlugin"
  SAVE ARTIFACT build/distributions/earthly-intellij-plugin-$version.zip AS LOCAL earthly-intellij-plugin-signed-$version.zip

publish:
  ARG --required version
  FROM --pass-args +sign
  RUN --push \
    --mount=type=cache,id=gradle_cache,target=/root/.gradle/caches \
    --secret CERTIFICATE_CHAIN=+secrets/earthly-technologies/intellij-plugin/chain.crt \
    --secret PRIVATE_KEY=+secrets/earthly-technologies/intellij-plugin/private.pem \
    --secret PRIVATE_KEY_PASSWORD=+secrets/earthly-technologies/intellij-plugin/private-key-password \
    --secret PUBLISH_TOKEN=+secrets/earthly-technologies/intellij-plugin/marketplace-token \
      DO gradle+RUN_WITH_CACHE --command="gradle --no-daemon publishPlugin"

ide:
  LOCALLY
  DO +GET_BUNDLE
  RUN gradle runIde

generate-gradle-wrapper:
  # Simply running 'wrapper' results in downloading the actual project dependencies,
  # which is a waste, so we create a dummy project and generate a wrapper from that.
  WORKDIR /tmp/wrap
  RUN gradle --no-daemon init wrapper
  SAVE ARTIFACT ./gradle AS LOCAL ./gradle
