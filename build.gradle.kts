plugins {
    kotlin("jvm") version "1.8.10"
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
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
}

mirai {
    jvmTarget = JavaVersion.VERSION_17
}
