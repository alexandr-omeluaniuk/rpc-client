pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

include(":module:boundary:calculator")
include(":module:libs:rpc-core")
include(":module:libs:rpc-transport-http")
include(":module:libs:discovery-core")
include(":module:libs:discovery-client")
include(":module:microservice:calculator-microservice")
include(":module:microservice:gateway-microservice")
include(":module:microservice:discovery-microservice")

rootProject.name = "rpc-client"