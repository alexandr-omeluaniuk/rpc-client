package ss.rpc.calculator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ss.discovery.core.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
class CalculatorApp

fun main(args: Array<String>) {
    runApplication<CalculatorApp>(*args)
}