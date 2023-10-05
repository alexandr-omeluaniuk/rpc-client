package ss.rpc.discovery.service

import org.springframework.stereotype.Service
import ss.discovery.core.RpcRoute

@Service
class DiscoveryService {

    private val routingTable = HashMap<String, RpcRoute>()

    fun registerRpcCall(name: String, host: String, port: Int) {
        routingTable.put(name, RpcRoute(name, host, port))
    }
}