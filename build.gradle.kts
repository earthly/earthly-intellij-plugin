plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.15.0"
}

dependencies {
    implementation("org.apache.commons:commons-collections4:4.4")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.1")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.1")
}

sourceSets["main"].java.srcDirs("src/main/gen")

group = "dev.earthly"
version = "0.0.0"

repositories {
    mavenCentral()
}

// Configure Gradle IntelliJ Plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
intellij {
    version.set("2022.3")
    type.set("GO") // Target IDE Platform
    plugins.set(listOf("org.jetbrains.plugins.textmate","org.jetbrains.plugins.go"))
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }


    patchPluginXml {
        sinceBuild.set("212")
        untilBuild.set("232.*")
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
        useJUnitPlatform()
        include("dev/earthly/plugin/lexer/**")
    }
}
