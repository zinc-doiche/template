

plugins {
    id("library")
    alias(libs.plugins.kotlinPluginSerialization)
}

dependencies {
    compileOnly(rootProject.libs.bundles.kotlinxEcosystem)
    compileOnly(rootProject.libs.apacheKafka)
    compileOnly(project(":utils:config"))
    testImplementation(kotlin("test"))
}
