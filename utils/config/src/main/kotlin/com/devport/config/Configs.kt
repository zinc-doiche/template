package com.devport.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.File
import java.io.InputStream
import kotlin.reflect.KClass

internal const val defaultPackage = "com.devport.resources."
internal val objectMapper = ObjectMapper(YAMLFactory()).registerKotlinModule()

data class ConfigSpec<T: Any>(
    val dataFolder: File,
    val extension: String,
    val clazz: Class<T>
) {
    constructor(dataRootInfo: DataRootInfo, clazz: Class<T>): this(dataRootInfo.dataFolder, dataRootInfo.extension, clazz)

    fun file(): File? {
        val simpleName = clazz.simpleName
        val name = clazz.canonicalName
            ?.replace(defaultPackage, "")
            ?.replace(simpleName, simpleName.toKebabCase())
            ?.replace(".", "/") ?: return null

        return File(dataFolder, "$name.$extension")
    }

    fun save(defaultResource: InputStream) {
        file()?.let { file ->
            file.outputStream().use { outputStream ->
                defaultResource.copyTo(outputStream)
            }
        }
    }

    fun load(): T? {
        return file()?.let {
            objectMapper.readValue(it, clazz)
        }
    }

    fun String.toCamelCase() = split("_").joinToString("") {
        replaceFirstChar { char ->
            if (char.isLowerCase()) {
                char.titlecase()
            } else {
                char.toString()
            }
        }
    }

    fun String.toKebabCase() = replace(Regex("([a-z])([A-Z])")) {
        "${it.groupValues[1]}_${it.groupValues[2]}"
    }.lowercase()
}

fun <T: Any> KClass<T>.asConfigSpec(dataRootInfo: DataRootInfo) = ConfigSpec(dataRootInfo, this.java)