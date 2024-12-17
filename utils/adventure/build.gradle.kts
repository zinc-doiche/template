plugins {
    id("library")
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(rootProject.libs.bundles.kotlinxEcosystem)
    compileOnly("net.kyori:adventure-text-minimessage:4.17.0")
    compileOnly("net.kyori:adventure-api:4.17.0")
}
