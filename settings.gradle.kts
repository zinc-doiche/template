// The settings file is the entry point of every Gradle build.
// Its primary purpose is to define the subprojects.
// It is also used for some aspects of project-wide configuration, like managing plugins, dependencies, etc.
// https://docs.gradle.org/current/userguide/settings_file_basics.html

dependencyResolutionManagement {
    // Use Maven Central as the default repository (where Gradle will download dependencies) in all subprojects.
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
    }
}

plugins {
    // Use the Foojay Toolchains plugin to automatically download JDKs required by subprojects.
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

// Include the `app` and `utils` subprojects in the build.
// If there are changes in only one of the projects, Gradle will rebuild only the one that has changed.
// Learn more about structuring projects with Gradle - https://docs.gradle.org/8.7/userguide/multi_project_builds.html

rootProject.name = "template"

include(":proxy")
include(":kafka")

include(":utils")
include(":utils:annotation")
include(":utils:adventure")
include(":utils:config")
include(":utils:processor")
include(":utils:test")

include(":storage")
include(":storage:cache")
include(":storage:history")
include(":storage:local:api")
include(":storage:local:core")
include(":storage:database")
include(":storage:database:api")
include(":storage:database:core")

include(":game")
include(":game:core")
include(":game:activity")
include(":game:hub")