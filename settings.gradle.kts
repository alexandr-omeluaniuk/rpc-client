pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

include(":module:calculator-boundary")
include(":module:calculator-microservice")
include(":module:gateway-microservice")
include(":module:discovery-microservice")

rootProject.name = "rpc-client"