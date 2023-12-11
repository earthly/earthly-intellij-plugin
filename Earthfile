VERSION --pass-args --global-cache 0.7
PROJECT earthly-technologies/core
IMPORT github.com/earthly/lib/gradle:83b97695005665da1e97d0e4b8ac2213868f649316d9e15576a4e600126ab2ee AS gradle
ARG --global gradle_version=8.2.1
ARG --global bundle="github.com/earthly/earthfile-grammar+export/"

install:
  FROM gradle:${gradle_version}-jdk17
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
  COPY $bundle build/
  RUN scripts/bundle.sh build/earthfile-syntax-highlighting

dist:
  FROM +src
  ARG version=0.0.0
  RUN sed -i 's^0.0.0^'"$version"'^g' ./build.gradle.kts
  RUN --mount=$EARTHLY_GRADLE_USER_HOME_CACHE --mount=$EARTHLY_GRADLE_PROJECT_CACHE gradle --no-daemon buildPlugin
  SAVE ARTIFACT build/distributions/earthly-intellij-plugin-$version.zip AS LOCAL earthly-intellij-plugin-$version.zip

sign:
  ARG --required version
  FROM +src
  RUN --mount=$EARTHLY_GRADLE_USER_HOME_CACHE --mount=$EARTHLY_GRADLE_PROJECT_CACHE \
    --secret CERTIFICATE_CHAIN=intellij-plugin/chain.crt \
    --secret PRIVATE_KEY=intellij-plugin/private.pem \
    --secret PRIVATE_KEY_PASSWORD=intellij-plugin/private-key-password \
    gradle --no-daemon signPlugin
  SAVE ARTIFACT build/distributions/earthly-intellij-plugin-$version.zip AS LOCAL earthly-intellij-plugin-signed-$version.zip

publish:
  ARG --required version
  FROM --pass-args +sign
  RUN --push \
    $EARTHLY_GRADLE_MOUNT_CACHE \
    --secret CERTIFICATE_CHAIN=intellij-plugin/chain.crt \
    --secret PRIVATE_KEY=intellij-plugin/private.pem \
    --secret PRIVATE_KEY_PASSWORD=intellij-plugin/private-key-password \
    --secret PUBLISH_TOKEN=intellij-plugin/marketplace-token \
    gradle --no-daemon publishPlugin

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
