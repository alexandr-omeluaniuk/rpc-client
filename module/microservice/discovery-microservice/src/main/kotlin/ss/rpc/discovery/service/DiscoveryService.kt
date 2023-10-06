package ss.rpc.discovery.service

import org.springframework.stereotype.Service
import ss.rpc.discovery.core.RpcRoute

@Service
class DiscoveryService {

    private val routingTable = HashMap<String, RpcRoute>()

    fun registerRpcCall(rpcCallSignatures: List<String>, host: String, port: Int) {
        rpcCallSignatures.forEach {
            routingTable[it] = RpcRoute(it, host, port)
        }
        println(routingTable)
    }
}