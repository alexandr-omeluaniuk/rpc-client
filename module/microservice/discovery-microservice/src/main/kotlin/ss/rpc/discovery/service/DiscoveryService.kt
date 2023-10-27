package ss.rpc.discovery.service

import org.springframework.stereotype.Service
import ss.rpc.core.state.RoutingTable
import ss.rpc.core.state.RpcRoute
import ss.rpc.discovery.core.RpcRegistrationInfo

@Service
class DiscoveryService {

    fun registerRpcCall(rpcRegistrationInfo: RpcRegistrationInfo, host: String): List<RpcRoute> {
        val routes = rpcRegistrationInfo.signatures.map {
            RpcRoute(it, host, rpcRegistrationInfo.port)
        }
        val routingTable = RoutingTable.getInstance()
        routingTable.append(routes)
        return routingTable.getAll()
    }
}