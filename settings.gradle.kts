pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

include(":module:calculator-boundary")
include(":module:calculator-microservice")

rootProject.name = "rpc-client"