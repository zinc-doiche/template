

plugins {
    id("template") apply false
    id("game") apply false
    kotlin("jvm")
}

subprojects {
    apply {
        plugin("template")
        plugin("game")
    }

    dependencies {
        implementation(project(":storage:database:api"))
        implementation(project(":storage:local:api"))
    }


}