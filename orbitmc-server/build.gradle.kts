plugins {
    id("io.github.orbitemc.satellite") version "1.0.0-SNAPSHOT"
}

tasks.compileJava {
    options.isDeprecation = false
    options.isWarnings = false
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
