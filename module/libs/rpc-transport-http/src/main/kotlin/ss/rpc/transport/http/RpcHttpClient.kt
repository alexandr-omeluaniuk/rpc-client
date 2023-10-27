package ss.rpc.transport.http

import org.slf4j.LoggerFactory
import ss.rpc.core.RpcCallSignature
import ss.rpc.core.api.RpcClientProxy
import ss.rpc.core.state.RoutingTable
import java.lang.reflect.Method

class RpcHttpClient : RpcClientProxy {

    private val logger = LoggerFactory.getLogger(RpcHttpClient::class.java)

    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any {
        logger.debug("Call RPC [$method]")
        logger.debug("Arguments: ")
        args?.forEach { logger.debug(it.toString()) }
        val rpcSignature = RpcCallSignature(
            rpcService = method!!.declaringClass,
            method = method!!
        ).toString()
        logger.debug("RPC signature [$rpcSignature]")
        val route = RoutingTable.getInstance().getRoute(rpcSignature)
        logger.debug(route.toString())
        return 0
    }
}
