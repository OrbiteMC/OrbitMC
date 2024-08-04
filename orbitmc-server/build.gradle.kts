import kotlin.math.log

plugins {
    id("io.github.orbitemc.satellite") version "1.0.0-SNAPSHOT"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://maven.fabricmc.net")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testCompileOnly("org.jetbrains:annotations:24.0.0")

    compileOnly("org.jetbrains:annotations:24.0.0")
    bundled(project(":orbitmc-api"))
}

satellite {
    minecraftVersion = "1.21"
}

tasks.test {
    useJUnitPlatform()
}

tasks.buildChecksums {
    val foreignJarTask = project(":orbitmc-api").tasks.getByName("jar")
    val from = inputFiles.files.toMutableList()
    from.add(foreignJarTask.outputs.files.singleFile)
    inputFiles.setFrom(from)

    dependsOn(foreignJarTask)
}

tasks.assembleBootstrap {
    manifest {
        attributes["Main-Class"] = "io.github.orbitemc.bootstrap.Bootstrap"
    }
}
