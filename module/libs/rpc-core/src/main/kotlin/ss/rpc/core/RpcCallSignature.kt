package ss.rpc.core

import java.lang.reflect.Method

data class RpcCallSignature(
    val rpcService: Class<*>,
    val method: Method
)