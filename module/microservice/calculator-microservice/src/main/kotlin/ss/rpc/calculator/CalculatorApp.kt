package ss.rpc.calculator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CalculatorApp

fun main(args: Array<String>) {
    runApplication<CalculatorApp>(*args)
}