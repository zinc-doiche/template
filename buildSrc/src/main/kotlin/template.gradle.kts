import lib.libs
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.internal.KaptTask
import plugin.ResourceMirror

plugins {
    kotlin("jvm")
    kotlin("kapt")
    java
}

ResourceMirror().apply(project)

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(project(":utils:annotation"))
    implementation(project(":utils:processor"))
    implementation(project(":utils:adventure"))
    compileOnly(libs.bundles.annotationProcessors)
    compileOnly(libs.apacheKafka)

    kapt(project(":utils:processor"))
}

tasks {
    compileJava {
        options.release = 21
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
    }
}
