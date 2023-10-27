package ss.rpc.discovery.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ss.rpc.core.state.RoutingTable
import ss.rpc.core.state.RpcRoute
import ss.rpc.discovery.core.RpcRegistrationInfo

@Service
class DiscoveryService {

    private val logger = LoggerFactory.getLogger(DiscoveryService::class.java)

    fun registerRpcCall(rpcRegistrationInfo: RpcRegistrationInfo, host: String): List<RpcRoute> {
        val routes = rpcRegistrationInfo.signatures.map {
            RpcRoute(it, host, rpcRegistrationInfo.port).also { route ->
                logger.debug("Register new route [$route]")
            }
        }
        val routingTable = RoutingTable.getInstance()
        routingTable.append(routes)
        return routingTable.getAll()
    }
}