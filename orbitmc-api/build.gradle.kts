import kotlin.math.log

plugins {
    `java-library`
}

val minecraftVersion = "1.21"

repositories {
    mavenLocal()
    mavenCentral()
}

val orbitmcServer = project(":orbitmc-server")
val exclusionPatterns = setOf(
    "com.github.oshi:oshi-core",
    "com.mojang:authlib",
    "com.mojang:datafixerupper",
    "com.mojang:logging",
    "commons-io:commons-io",
    "io.netty",
    "net.java.dev.jna",
    "net.sf.jopt-simple",
    "org.apache.commons:commons-lang3",
    "org.lz4:lz4-java"
)
dependencies {
    compileOnly("org.jetbrains:annotations:24.0.0")
    // This should probably be in satellite in a smarter form
    val file = orbitmcServer.layout.buildDirectory.file("satellite").get().asFile.resolve(minecraftVersion).resolve("libraries.list")
    file.readLines().map { it.split("\t")[1] }.filter {
        for (exclusionPattern in exclusionPatterns) {
            if (it.contains(exclusionPattern)) {
                logger.lifecycle("excluding $it")
                return@filter false
            }
        }
        return@filter true
    }.forEach { compileOnly(it) }
}
