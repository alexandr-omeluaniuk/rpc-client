package ss.rpc.discovery.service

import org.springframework.stereotype.Service
import ss.rpc.discovery.core.RpcRegistrationInfo
import ss.rpc.discovery.core.RpcRoute

@Service
class DiscoveryService {

    private val routingTable = HashMap<String, RpcRoute>()

    fun registerRpcCall(rpcRegistrationInfo: RpcRegistrationInfo, host: String): List<RpcRoute> {
        rpcRegistrationInfo.signatures.forEach {
            routingTable[it] = RpcRoute(it, host, rpcRegistrationInfo.port)
        }
        return routingTable.values.toList()
    }
}