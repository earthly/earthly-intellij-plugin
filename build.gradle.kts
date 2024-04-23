plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.17.3"
    id("antlr")
}

dependencies {
    antlr("org.antlr:antlr4:4.10.1")
    implementation("org.apache.commons:commons-collections4:4.4")
    implementation("org.antlr:antlr4-intellij-adaptor:0.1")
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
        untilBuild.set("241.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }

    generateGrammarSource {
        arguments = arguments + listOf("-visitor", "-package", "dev.earthly.plugin.language.syntax.parser")
        outputDirectory = layout.buildDirectory.dir("generated-src/antlr/main/dev/earthly/plugin/language/syntax/parser").get().asFile
        maxHeapSize = "64m"
    }

    test {
        useJUnit()
    }
}
