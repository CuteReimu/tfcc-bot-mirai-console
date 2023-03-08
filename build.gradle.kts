plugins {
    kotlin("jvm") version "1.8.0"
    application
    kotlin("plugin.serialization") version "1.8.0"
    id("net.mamoe.mirai-console") version "2.14.0"
}

group = "org.tfcc.bot"
version = "2.0.1"

repositories {
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
}

dependencies {
    implementation("log4j:log4j:1.2.17")
}

mirai {
    jvmTarget = JavaVersion.VERSION_17
}
