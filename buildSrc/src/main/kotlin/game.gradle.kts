import io.papermc.paperweight.userdev.ReobfArtifactConfiguration
import lib.YamlSpec
import lib.libs
import xyz.jpenilla.resourcefactory.bukkit.BukkitPluginYaml
import xyz.jpenilla.resourcefactory.bukkit.bukkitPluginYaml

plugins {
    id("io.papermc.paperweight.userdev")
    id("xyz.jpenilla.resource-factory-bukkit-convention")
}

paperweight {
    reobfArtifactConfiguration = ReobfArtifactConfiguration.MOJANG_PRODUCTION
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper)
}

bukkitPluginYaml {
    main = "${YamlSpec.ROOT_PACKAGE}.MainKt"
    load = BukkitPluginYaml.PluginLoadOrder.STARTUP
    authors.addAll(YamlSpec.AUTHORS)
    apiVersion = libs.versions.paper.map { it.substring(0, 4) }
}
