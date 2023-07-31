plugins {
    kotlin("jvm") version "1.8.22"
    application
    kotlin("plugin.serialization") version "1.8.22"
    id("net.mamoe.mirai-console") version "2.14.0"
}

group = "org.tfcc.bot"
version = "2.0.1"

repositories {
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.google.zxing:core:3.5.2")
}

mirai {
    jvmTarget = JavaVersion.VERSION_17
}
