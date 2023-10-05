package ss.rpc.boundary

import ss.rpc.core.RpcService

@RpcService
interface CalculatorService {

    fun sum(a: Int, b: Int): Int

    fun multiplication(a: Int, b: Int): Int
}