rootProject.name = "OrbitMC"
gradle.rootProject {
    group = "io.github.orbitemc"
    version = "1.0.0-SNAPSHOT"
}

dependencyResolutionManagement {
    pluginManagement {
        repositories {
            mavenLocal()
            mavenCentral()
            gradlePluginPortal()
            maven("https://maven.fabricmc.net")
        }
    }
}

rootProject.projectDir.listFiles()!!.filter { it.isDirectory }.forEach { file ->
    if (file.name == "buildSrc") {
        return@forEach
    }

    if (file.listFiles()!!.any { it.name.contains("build.gradle") }) {
        include(":${file.name}")
    }
}
