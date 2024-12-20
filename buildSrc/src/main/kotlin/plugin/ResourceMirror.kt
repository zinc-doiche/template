package plugin

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.gradle.api.Plugin
import org.gradle.api.Project
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import gradle.kotlin.dsl.accessors._602de17f7470c999abbd1d0b8f55665a.compileKotlin
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File
import javax.inject.Inject
import org.gradle.api.tasks.Internal
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.internal.KaptGenerateStubsTask
import org.jetbrains.kotlin.gradle.internal.KaptTask
import org.jetbrains.kotlin.gradle.tasks.KotlinTest

open class ResourcePluginExtension {
    var packageName: String = "com.devport.resources"
    var resourceDirectory: String = "src/main/resources"
}

class ResourceMirror : Plugin<Project> {
    override fun apply(target: Project) {
        val pluginExtension = target.extensions.create("resourcePlugin", ResourcePluginExtension::class.java)

        with(target.tasks) {
            register<ResourceMirrorTask>(
                "generateResourcesData",
                ResourceMirrorTask::class.java,
                pluginExtension
            ).configure {
                dependsOn("kaptKotlin")
            }
//            withType(KaptTask::class) {
//                dependsOn("generateResourcesData")
//            }
            compileKotlin.configure {
                dependsOn("generateResourcesData")
            }
            // kaptKotlin -> generateResourcesData -> compileKotlin
        }
    }
}

open class ResourceMirrorTask @Inject constructor(
    @Internal
    val pluginExtension: ResourcePluginExtension
) : DefaultTask() {

    @InputFiles
    val inputFiles = project.fileTree("src/main/resources")

    @OutputDirectory
    val outputDir = project.layout.buildDirectory.file("generated/source/kapt/main")

    @TaskAction
    fun mirrorResources() {
        val projectDir = inputFiles.asPath.split("src/main/resources")[0]
        val objectMapper = ObjectMapper(YAMLFactory()).registerKotlinModule()
        val resourcesFolder = File(projectDir, pluginExtension.resourceDirectory)
        val outputFolder = outputDir.get().asFile
        val packageName = pluginExtension.packageName
        val fileSpecs = mutableListOf<FileSpec>()
        val targetPackage = File(outputFolder, pluginExtension.packageName.replace(".", "/"))

        resourcesFolder.walkTopDown().forEach { file ->
            if(file.isDirectory) {
                file.mkdirs()
            } else {
                fileSpecs.add(file.mirrorObject(objectMapper, packageName))
            }
        }

        if(targetPackage.exists()) {
            targetPackage.deleteRecursively()
        }

        fileSpecs.forEach { fileSpec ->
            fileSpec.writeTo(outputFolder)
        }
    }

    fun File.mirrorObject(objectMapper: ObjectMapper, packageName: String): FileSpec {
        @Suppress("UNCHECKED_CAST")
        val data = objectMapper.readValue(this, Any::class.java) as Map<String, Any>
        val modifiedPackageName = "$packageName${getAbstractPath()}"
        val specs = apart(data, modifiedPackageName, nameWithoutExtension)
        val name = nameWithoutExtension.replaceFirstChar { it.uppercase() }
        val typeSpec = TypeSpec.classBuilder(name)
            .addModifiers(KModifier.DATA)
            .primaryConstructor(specs.first)
            .addProperties(specs.second)
            .addTypes(specs.third)
            .build()

        return FileSpec.builder(modifiedPackageName, name)
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
        parentName: String
    ): Triple<FunSpec, List<PropertySpec>, List<TypeSpec>> {
        val parameters = mutableListOf<ParameterSpec>()
        val properties = mutableListOf<PropertySpec>()
        val types = mutableListOf<TypeSpec>()
        var finalPackageName = "$packageName.${parentName.replaceFirstChar { it.uppercase() }}"

        data.forEach { (key, value) ->
            when(value) {
                is Map<*, *> -> {
                    val className = key.replaceFirstChar { it.uppercase() }
                    @Suppress("UNCHECKED_CAST")
                    val apart = apart(value as Map<String, Any>, finalPackageName, className)
                    val typeSpec = TypeSpec
                        .classBuilder(className)
                        .primaryConstructor(apart.first)
                        .addModifiers(KModifier.DATA)
                        .addProperties(apart.second)
                        .addTypes(apart.third)
                        .build()
                    val type = ClassName(finalPackageName, typeSpec.name!!)

                    org.gradle.internal.cc.base.logger.lifecycle("[$parentName] APART [$finalPackageName] : \n\t $key -> $className ")

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