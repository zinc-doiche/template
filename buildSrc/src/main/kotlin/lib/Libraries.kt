package lib

//object LibrariesSpec {
//    const val PAPER_VERSION = "1.21.4-R0.1-SNAPSHOT"
//
//    val AUTO_SERVICE = LibrarySpec(
//        id = "com.google.auto.service:auto-service",
//        version = "1.1.1"
//    )
//    val KOTLIN_POET = LibrarySpec(
//        id = "com.squareup:kotlinpoet",
//        version = "2.0.0"
//    )
//    val REFLECTIONS = LibrarySpec(
//        id = "org.reflections:reflections",
//        version = "0.9.12"
//    )
//    val KAFKA = LibrarySpec(
//        id = "org.apache.kafka:kafka-clients",
//        version = "3.8.1"
//    )
//
//    fun apiVersion() = PAPER_VERSION.split("-")[0]
//}

object YamlSpec {
    const val ROOT_PACKAGE = "com.devport"
    val AUTHORS = listOf(
        "doiche"
    )
}

data class LibrarySpec(
    val id: String,
    val version: String
) {
    fun string(): String {
        return "$id:$version"
    }
}