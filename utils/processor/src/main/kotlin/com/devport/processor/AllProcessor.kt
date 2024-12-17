package com.devport.processor

import com.devport.annotation.All
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.DelicateKotlinPoetApi
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import org.reflections.Reflections
import org.reflections.scanners.MethodAnnotationsScanner
import org.reflections.scanners.SubTypesScanner
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@AutoService(Processor::class)
class AllProcessor : AbstractProcessor() {

    override fun getSupportedAnnotationTypes(): Set<String> {
        return setOf(All::class.java.name)
    }

    override fun getSupportedSourceVersion(): SourceVersion? {
        return SourceVersion.latest()
    }

    @OptIn(DelicateKotlinPoetApi::class)
    override fun process(
        annotations: Set<TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        roundEnv.getElementsAnnotatedWith(All::class.java).forEach { element ->
            val messager = processingEnv.messager
            val methodName = element.simpleName.toString()
            val typeElement = element.enclosingElement as TypeElement
            val className = typeElement.asClassName()
            val fileName = "${className.simpleName}${methodName.replaceFirstChar(Char::uppercase)}s"

            if (typeElement.kind == ElementKind.INTERFACE) {
                val simpleName = className.simpleNames.first()
                val targetClassName = ClassName(className.canonicalName.replace(simpleName, ""), simpleName)

                messager.printMessage(
                    Diagnostic.Kind.NOTE,
                    "Generating $fileName.kt",
                    typeElement
                )

                FileSpec.builder("com.devport.generated", fileName)
                    .addType(
                        TypeSpec.objectBuilder(fileName)
                            .addFunction(
                                FunSpec.builder(methodName + "All")
                                    .addStatement(
                                        "val reflections = %T(\"com.devport\", %T(), %T())",
                                        Reflections::class.java,
                                        SubTypesScanner::class.java,
                                        MethodAnnotationsScanner::class.java
                                    )
                                    .addStatement("val list = reflections.getSubTypesOf(%T::class.java)", targetClassName)
                                    .beginControlFlow("for (clazz in list)")
                                    .addStatement("val constructor = clazz.getDeclaredConstructor()")
                                    .addStatement("val instance = constructor.newInstance() as %T", targetClassName)
                                    .addStatement("instance.%L()", methodName)
                                    .endControlFlow()
                                    .build()
                            )
                            .build()
                    )
                    .build()
                    .writeTo(processingEnv.filer)
            }
        }
        return false
    }
}