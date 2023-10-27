package ss.rpc.transport.http.model

data class RpcCall(
    val rpcCallName: String,
    val args: Array<out Any>? = null
)