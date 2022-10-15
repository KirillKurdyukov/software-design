plugins {
    kotlin("jvm") version "1.6.10"
    java
}

group = "ru.itmo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val ktorVersion = "1.6.8"

dependencies {
    // Default
    implementation(kotlin("stdlib"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    // Client
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.4")
    implementation("io.ktor:ktor-client-jackson:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-core:$ktorVersion")

    // Mockserver
    implementation("org.mock-server:mockserver-client-java:5.14.0")
    implementation("org.mock-server:mockserver-netty:5.14.0")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}