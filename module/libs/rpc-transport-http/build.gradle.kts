plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.0"
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":module:libs:rpc-core"))
    compileOnly("org.slf4j:slf4j-api:2.0.9")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")
    compileOnly("org.springframework.boot:spring-boot-starter-web:3.1.4")
}