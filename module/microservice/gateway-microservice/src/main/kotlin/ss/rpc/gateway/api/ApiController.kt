package ss.rpc.gateway.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import ss.rpc.boundary.CalculatorService

@RestController
class ApiController(
    private val calculatorService: CalculatorService
) {

    @GetMapping("/calc/sum/{a}/{b}")
    fun sum(
        @PathVariable("a") a: Int,
        @PathVariable("b") b: Int
    ): Int {
        return calculatorService.sum(a, b)
    }
}