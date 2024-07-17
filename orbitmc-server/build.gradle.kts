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
    compileOnly("org.jetbrains:annotations:24.0.0")
}

satellite {
    minecraftVersion = "1.21"
}

tasks.assembleBootstrap {
    manifest {
        attributes["Main-Class"] = "io.github.orbitemc.bootstrap.Bootstrap"
    }
}
