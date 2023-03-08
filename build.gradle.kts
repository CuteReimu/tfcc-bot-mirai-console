import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.0"
    application
    kotlin("plugin.serialization") version "1.8.0"
    id("net.mamoe.mirai-console") version "2.9.2"
}

group = "org.tfcc.bot"
version = "1.0.3"

repositories {
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
}

dependencies {
    implementation("log4j:log4j:1.2.17")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}
