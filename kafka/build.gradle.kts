import plugin.ResourceMirror

plugins {
    id("library")
    alias(libs.plugins.kotlinPluginSerialization)
}

ResourceMirror().apply(project)

dependencies {
    compileOnly(rootProject.libs.bundles.kotlinxEcosystem)
    compileOnly(rootProject.libs.apacheKafka)
    testImplementation(kotlin("test"))
}
