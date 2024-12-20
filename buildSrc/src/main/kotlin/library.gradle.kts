import lib.libs

plugins {
    kotlin("jvm")
    kotlin("kapt")
    `java-library`
}

kotlin {
    jvmToolchain(21)
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    compileOnly(libs.bundles.annotationProcessors)
    testImplementation(libs.bundles.annotationProcessors)
}

tasks {
    compileJava {
        options.release = 21
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
    }
}