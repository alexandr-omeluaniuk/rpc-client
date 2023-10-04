pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

include(":module:libs:calculator-boundary")
include(":module:microservice:calculator-microservice")
include(":module:microservice:gateway-microservice")
include(":module:microservice:discovery-microservice")

rootProject.name = "rpc-client"