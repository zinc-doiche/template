package plugin

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.gradle.api.Plugin
import org.gradle.api.Project
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import java.io.File

class ResourceMirror : Plugin<Project> {
    private val objectMapper = ObjectMapper(YAMLFactory()).registerKotlinModule()

    override fun apply(target: Project) {
        val extension = target.extensions.create("resourcePlugin", ResourcePluginExtension::class.java)

        target.tasks.create("generateResourcesData") {
            val resourcesFolder = File(project.projectDir, extension.resourceDirectory)
            val packageName = extension.packageName
            val fileSpecs = mutableListOf<FileSpec>()

            resourcesFolder.walkTopDown().forEach { file ->
                if(file.isDirectory) {
                    file.mkdirs()
                } else {
                    fileSpecs.add(file.mirrorObject(packageName))
                }
            }

            File(project.layout.buildDirectory.asFile.get(), "generated/source/kapt/main/").apply {
                val targetPackage = File(this, extension.packageName.replace(".", "/"))

                if(targetPackage.exists()) {
                    targetPackage.deleteRecursively()
                }

                fileSpecs.forEach { fileSpec ->
                    fileSpec.writeTo(this)
                }
            }
        }
    }

    fun File.mirrorObject(packageName: String): FileSpec {
        val data = objectMapper.readValue(this, Any::class.java) as Map<String, Any>
        val packageName = "$packageName.${getAbstractPath()}"
        val specs = apart(data, packageName, nameWithoutExtension)
        val typeSpec = TypeSpec
            .classBuilder(nameWithoutExtension.replaceFirstChar { it.uppercase() })
            .primaryConstructor(specs.first)
            .addProperties(specs.second)
            .addTypes(specs.third)
            .build()

        return FileSpec.builder(packageName, nameWithoutExtension)
            .addType(typeSpec)
            .build()
    }

    fun File.getAbstractPath(): String {
        val parent = parentFile?.takeIf { it.nameWithoutExtension != "resources" } ?: return ""
        val prePath = parent.getAbstractPath()
        val name = parent.nameWithoutExtension
        return "$prePath.$name"
    }

    fun apart(
        data: Map<String, Any>,
        packageName: String,
        fileName: String
    ): Triple<FunSpec, List<PropertySpec>, List<TypeSpec>> {
        val parameters = mutableListOf<ParameterSpec>()
        val properties = mutableListOf<PropertySpec>()
        val types = mutableListOf<TypeSpec>()

        data.forEach { (key, value) ->
            val className = key.replaceFirstChar { it.uppercase() }
            val packageName = "$packageName.${fileName.replaceFirstChar { it.uppercase() }}"

            when(value) {
                is Map<*, *> -> {
                    val apart = apart(value as Map<String, Any>, packageName, fileName)
                    val typeSpec = TypeSpec
                        .classBuilder(className)
                        .primaryConstructor(apart.first)
                        .addProperties(apart.second)
                        .addTypes(apart.third)
                        .build()
                    val type = ClassName(packageName, typeSpec.name!!)

                    types.add(typeSpec)
                    parameters.add(
                        ParameterSpec.builder(key, type)
                            .build()
                    )
                    properties.add(
                        PropertySpec.builder(key, type)
                            .initializer(key)
                            .build()
                    )
                }
                is List<*> -> {
                    val typeName = getNestedType(value)
                    parameters.add(
                        ParameterSpec.builder(key, typeName)
                            .build()
                    )
                    properties.add(
                        PropertySpec.builder(key, typeName)
                            .initializer(key)
                            .build()
                    )
                }
                else -> {
                    parameters.add(
                        ParameterSpec.builder(key, value::class)
                            .build()
                    )
                    properties.add(
                        PropertySpec.builder(key, value::class)
                            .initializer(key)
                            .build()
                    )
                }
            }
        }
        return Triple(
            FunSpec.constructorBuilder()
                .addParameters(parameters)
                .build(),
            properties,
            types
        )
    }

    fun getNestedType(data: Any): TypeName {
        return when(data) {
            is Map<*, *> -> {
                val valueClassCandidate = data.values.first()!!::class

                Map::class.asTypeName()
                    .parameterizedBy(
                        getNestedType(data.keys.first()!!),
                        if(data.values.all { it!!::class == valueClassCandidate }) {
                            getNestedType(valueClassCandidate)
                        } else {
                            Any::class.asTypeName()
                        }
                    )
            }
            is List<*> -> {
                val classCandidate = data.first()!!::class

                List::class.asTypeName()
                    .parameterizedBy(
                        if(data.all { it!!::class == classCandidate }) {
                            getNestedType(classCandidate)
                        } else {
                            Any::class.asTypeName()
                        }
                    )
            }
            else -> data::class.asTypeName()
        }
    }
}

open class ResourcePluginExtension {
    var packageName: String = "com.devport.resources"
    var resourceDirectory: String = "src/main/resources"
}