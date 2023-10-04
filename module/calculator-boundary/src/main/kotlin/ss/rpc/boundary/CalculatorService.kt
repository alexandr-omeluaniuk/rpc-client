package ss.rpc.boundary

interface CalculatorService {

    fun sum(a: Int, b: Int): Int

    fun multiplication(a: Int, b: Int): Int
}