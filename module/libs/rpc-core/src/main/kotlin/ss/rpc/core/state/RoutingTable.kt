package ss.rpc.core.state

import java.util.concurrent.ConcurrentHashMap

class RoutingTable {

    private val routingTable: MutableMap<String, RpcRoute> = ConcurrentHashMap()

    fun update(routes: List<RpcRoute>) {
        routingTable.clear()
        routingTable.putAll(routes.associateBy(RpcRoute::rpcCallName))
    }

    fun append(routes: List<RpcRoute>) {
        routingTable.putAll(routes.associateBy(RpcRoute::rpcCallName))
    }

    fun getAll(): List<RpcRoute> = routingTable.values.toList()

    fun getRoute(rpcCallName: String): RpcRoute? = routingTable[rpcCallName]

    companion object {

        @Volatile
        private var instance: RoutingTable? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: RoutingTable().also { instance = it }
        }
    }
}