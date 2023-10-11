plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.0"
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot:3.1.4")
    implementation(project(":module:libs:discovery-core"))
    implementation(project(":module:libs:rpc-transport-http"))
    implementation("com.fasterxml.jackson.core:jackson-core:2.15.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")
    implementation("org.slf4j:slf4j-api:2.0.9")
}