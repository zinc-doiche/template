package com.devport.config

import com.devport.config.Configs.defaultPackage
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.File
import java.io.InputStream

object Configs {
    const val defaultPackage = "com.devport.resources."
    val objectMapper = ObjectMapper(YAMLFactory()).registerKotlinModule()

    fun <T: Any> save(defaultResource: InputStream, configSpec: ConfigSpec<T>) {
        configSpec.file()?.let {
            it.outputStream().use {
                outputStream -> defaultResource.copyTo(outputStream)
            }
        }
    }

    fun <T: Any> load(configSpec: ConfigSpec<T>): T? {
        return configSpec.file()?.let {
            objectMapper.readValue(it, configSpec.clazz)
        }
    }
}

data class ConfigSpec<T: Any>(
    val dataFolder: File,
    val extension: String,
    val clazz: Class<T>
) {
    fun file(): File? {
        val name = clazz.canonicalName?.replace(defaultPackage, "")?.replace(".", "/") ?: return null
        return File(dataFolder, "$name.$extension")
    }
}