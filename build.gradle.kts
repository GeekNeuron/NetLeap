plugins {
    kotlin("jvm") version "1.9.22"
    application
}

group = "GeekNeuron"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.ktor:ktor-client-core:2.3.2")
    implementation("io.ktor:ktor-client-cio:2.3.2")
    implementation("io.ktor:ktor-client-websockets:2.3.2")
    implementation("io.ktor:ktor-client-logging:2.3.2")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("de.mkammerer:argon2-jvm:2.7")
    implementation("org.slf4j:slf4j-simple:2.0.12")

    testImplementation(kotlin("test"))
}

application {
    mainClass.set("GeekNeuron.client.MainKt")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}
