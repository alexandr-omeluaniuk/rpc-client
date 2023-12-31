plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.0"
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":module:libs:rpc-core"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")
}