package ss.rpc.transport.http

import org.slf4j.LoggerFactory
import ss.rpc.core.api.RpcClientProxy
import java.lang.reflect.Method

class RpcHttpClient : RpcClientProxy {

    private val logger = LoggerFactory.getLogger(RpcHttpClient::class.java)

    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any {
        logger.debug("Call RPC:$method")
        logger.debug("Arguments: ")
        args?.forEach { logger.debug(it.toString()) }
        return 0
    }
}
