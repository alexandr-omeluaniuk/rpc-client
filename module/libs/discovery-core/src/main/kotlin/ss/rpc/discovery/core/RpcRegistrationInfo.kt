package ss.rpc.discovery.core

data class RpcRegistrationInfo (
    val port: Int,
    val signatures: List<String>
)