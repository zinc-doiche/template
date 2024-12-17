import plugin.ResourceMirror

plugins {
    id("template")
    id("game") apply false
}

subprojects {
    apply {
        plugin("template")
        plugin("game")
        plugin(ResourceMirror::class.java)
    }

    dependencies {
        implementation(project(":storage:database:api"))
        implementation(project(":storage:local:api"))
    }
}