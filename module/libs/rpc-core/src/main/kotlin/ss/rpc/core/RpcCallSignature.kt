package ss.rpc.core

import java.lang.reflect.Method

data class RpcCallSignature(
    val method: Method
) {

    override fun toString(): String {
        val parameters = method.parameters.map {
            it.type
        }.joinToString(",")
        return String.format("%s::%s(%s):%s", method.declaringClass.name, method.name, parameters, method.returnType.name)
    }
}