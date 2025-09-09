plugins {
    java
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.18"
}

group = "com.andrewreedhall.ancientscrolls"
version = "1.0.0-SNAPSHOT"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    paperweight.paperDevBundle("1.21.8-R0.1-SNAPSHOT")
}

tasks.register<Jar>("buildTestPlugin") {
    group = "build"
    from(sourceSets.main.get().output)
    destinationDirectory.set(file("$projectDir/test-server/plugins"))
    archiveFileName.set(project.name + "-TEST.jar")
}
