package ss.rpc.transport.http

import io.swagger.v3.oas.annotations.Hidden
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ss.rpc.core.state.RpcImplementationRegistry
import ss.rpc.transport.http.model.RpcCall
import ss.rpc.transport.http.model.RpcResponse

@Hidden
@RestController
@RequestMapping(RPC_ENDPOINT)
class RpcHttpServer {

    private val logger = LoggerFactory.getLogger(RpcHttpServer::class.java)

    @PostMapping
    fun rpcCall(
        @RequestBody payload: RpcCall
    ): RpcResponse {
        logger.debug("Accept payload [$payload]")
        val parts = payload.rpcCallName.split("::")
        val methodName = parts[1].split("(")[0]
        val parametersStr = parts[1].substring(parts[1].indexOf("(") + 1, parts[1].indexOf(")"))
        val parameters = parametersStr.split(",").map {
            when(it) {
                "int" -> Int::class.java
                else -> Class.forName(it)
            }
        }
        val clazz = Class.forName(parts[0])
        val method = clazz.getMethod(methodName, *parameters.toTypedArray())
        val bean = RpcImplementationRegistry.getInstance().getBean(clazz)
        val args = payload.args
        val result = if (args != null) {
            method.invoke(bean, *args)
        } else {
            method.invoke(bean)
        }
        return RpcResponse(result)
    }
}