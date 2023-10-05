package ss.rpc.discovery.service

import org.springframework.stereotype.Service
import ss.rpc.discovery.core.RpcRoute

@Service
class DiscoveryService {

    private val routingTable = HashMap<String, RpcRoute>()

    fun registerRpcCall(name: String, host: String, port: Int) {
        routingTable[name] = RpcRoute(name, host, port)
    }
}