plugins {
    id("library")
    alias(libs.plugins.kotlinPluginSerialization)
}

subprojects {
    apply {
        plugin("library")
        plugin(rootProject.libs.plugins.kotlinPluginSerialization.get().pluginId)
    }

    dependencies {
        compileOnly(rootProject.libs.bundles.kotlinxEcosystem)
    }
}