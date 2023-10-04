package ss.rpc.gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DiscoveryApp

fun main(args: Array<String>) {
    runApplication<DiscoveryApp>(*args)
}