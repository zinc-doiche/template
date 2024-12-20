import plugin.ResourceMirror

plugins {
    id("library")
    alias(libs.plugins.kotlinPluginSerialization)
}

subprojects {
    apply {
        plugin("library")
        plugin(ResourceMirror::class)
        plugin(rootProject.libs.plugins.kotlinPluginSerialization.get().pluginId)
    }

    dependencies {
        compileOnly(rootProject.libs.bundles.kotlinxEcosystem)
        implementation(project(":utils:config"))

        testImplementation(kotlin("test"))
        testImplementation(project(":utils:test"))
        testImplementation("ch.qos.logback:logback-classic:1.4.12")
    }
}