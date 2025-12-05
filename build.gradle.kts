import org.gradle.api.plugins.JavaPluginExtension

plugins {
    id("org.springframework.boot") version "4.0.0-M1" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
    id("org.graalvm.buildtools.native") version "0.10.5" apply false
}

allprojects {
    group = "com.yourorg"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
        maven {
            name = "SpringMilestone"
            url = uri("https://repo.spring.io/milestone")
        }
    }
}

subprojects {
    apply(plugin = "java")

    extensions.configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(25))
        }
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

