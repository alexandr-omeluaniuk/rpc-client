package ss.rpc.calculator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan("ss")
class CalculatorApp

fun main(args: Array<String>) {
    runApplication<CalculatorApp>(*args)
}