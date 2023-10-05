package ss.rpc.discovery.core

data class RpcRoute(
    val rpcCallName: String,
    val host: String,
    val port: Int
)
