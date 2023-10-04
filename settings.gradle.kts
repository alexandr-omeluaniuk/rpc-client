pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

include(":module:calculator-boundary")
include(":module:calculator-microservice")
include(":module:gateway-microservice")

rootProject.name = "rpc-client"