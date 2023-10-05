package ss.rpc.core

import java.lang.reflect.Method

data class RpcCallSignature(
    val rpcService: Class<*>,
    val method: Method
) {

    override fun toString(): String {
        val parameters = method.parameters.map {
            it.type
        }.joinToString(",")
        return String.format("%s::%s(%s):%s", rpcService.name, method.name, parameters, method.returnType.name)
    }
}