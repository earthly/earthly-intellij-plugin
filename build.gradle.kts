plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.17.3"
}

dependencies {
    implementation("org.apache.commons:commons-collections4:4.4")
    testImplementation("junit:junit:4.13.2")
}

group = "dev.earthly"
version = "0.0.0"

repositories {
    mavenCentral()
}

// Configure Gradle IntelliJ Plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
intellij {
    version.set("2023.2.6")
    type.set("IU") // Target IDE Platform
    plugins.set(listOf("org.jetbrains.plugins.textmate"))
}


tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    patchPluginXml {
        sinceBuild.set("232")
        untilBuild.set("242.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }

    test {
        useJUnit()
    }
}
