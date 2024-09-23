plugins {
    kotlin("jvm") version "2.0.10"
    application
}

group = "dev.conorgarry"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("dev.conorgarry.MainKt")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.github.ajalt.clikt:clikt:5.0.0")
    implementation("com.squareup:kotlinpoet:1.12.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}

tasks.named("distTar").configure { enabled = false }
tasks.named("distZip").configure { enabled = false }
tasks.named("assemble").configure { dependsOn(tasks.named("installDist")) }
