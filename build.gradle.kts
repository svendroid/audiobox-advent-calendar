import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    application
}

group = "net.svenadolph"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("rocks.voss:toniebox-api:3.7")

    // audio file meta data reader
    implementation("com.mpatric:mp3agic:0.9.1")

    // rss parsing
    implementation("dev.stalla:stalla:1.1.0")

    // logging
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")
    implementation("ch.qos.logback:logback-classic:1.4.5")
    // bridge to access log4j logs in toniebox api
    implementation("org.slf4j:log4j-over-slf4j:2.0.5")

    // command line arguments parsing
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.5")

    // yaml parsing - https://github.com/FasterXML/jackson-dataformats-text/tree/2.15/yaml
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.14.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.14.0")

    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.13.3")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.25")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}