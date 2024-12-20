import org.jetbrains.kotlin.allopen.gradle.AllOpenExtension

plugins {
    kotlin("plugin.jpa") version libs.versions.kotlin apply false
    kotlin("plugin.allopen") version libs.versions.kotlin apply false
    kotlin("plugin.noarg") version libs.versions.kotlin apply false
}

subprojects {
    apply {
       plugin("org.jetbrains.kotlin.plugin.jpa")
       plugin("org.jetbrains.kotlin.plugin.allopen")
       plugin("org.jetbrains.kotlin.plugin.noarg")
    }

    extensions.configure(AllOpenExtension::class) {
        annotation("jakarta.persistence.Entity")
        annotation("jakarta.persistence.Embeddable")
        annotation("jakarta.persistence.MappedSuperclass")
    }

    dependencies {
        val libs = rootProject.libs

        // Implementation dependencies
        compileOnly(libs.mysql)
        compileOnly(libs.hibernate) {
            exclude(group = "cglib", module = "cglib")
            exclude(group = "asm", module = "asm")
        }
        compileOnly(libs.hikariCP)
        compileOnly(libs.bundles.jpql)
        compileOnly(libs.bundles.jarkarta)

        kapt(libs.jarkartaAnnotation)

        // Test dependencies
        testImplementation(libs.hikariCP)
        testImplementation(libs.bundles.jpql)
        testImplementation(libs.bundles.jarkarta)
        testImplementation(libs.mysql)
        testImplementation(libs.hibernate) {
            exclude(group = "cglib", module = "cglib")
            exclude(group = "asm", module = "asm")
        }
        testImplementation(libs.bundles.jacksons)

        kaptTest(libs.jarkartaAnnotation)
    }
}

