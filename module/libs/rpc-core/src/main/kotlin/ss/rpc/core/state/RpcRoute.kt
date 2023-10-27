package ss.rpc.core.state

data class RpcRoute(
    val rpcCallName: String,
    val host: String,
    val port: Int
)
