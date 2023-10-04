package ss.rpc.calculator.service

import org.springframework.stereotype.Service
import ss.rpc.boundary.CalculatorService

@Service
class CalculatorServiceImpl() : CalculatorService {
    override fun sum(a: Int, b: Int): Int {
        return a + b
    }
}