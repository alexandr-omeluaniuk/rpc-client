package ss.rpc.transport.http

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import ss.rpc.core.RpcCallSignature
import ss.rpc.core.api.RpcClientProxy
import ss.rpc.core.state.RoutingTable
import ss.rpc.core.state.RpcRoute
import java.lang.reflect.Method
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

class RpcHttpClient : RpcClientProxy {

    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any {
        logger.debug("Call RPC [$method]")
        logger.debug("Arguments: ")
        args?.forEach { logger.debug(it.toString()) }
        val rpcSignature = RpcCallSignature(
            method = method!!
        ).toString()
        logger.debug("RPC signature [$rpcSignature]")
        val route = RoutingTable.getInstance().getRoute(rpcSignature)!!
        logger.debug(route.toString())
        val payload = serializeArguments(args)
        logger.debug("Request payload [$payload]")
        val response = sendRequest(payload, route)
        logger.debug("Response [$response]")
        return 0
    }

    private fun sendRequest(payload: String?, route: RpcRoute): String? {
        val uri = URI("http://${route.host}:${route.port}$RPC_ENDPOINT")
        return httpClient.send(
            HttpRequest.newBuilder().uri(
                uri
            ).header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(payload)).build(),
            HttpResponse.BodyHandlers.ofString()
        ).body()
    }

    private fun serializeArguments(args: Array<out Any>?): String {
        return args?.let { objectMapper.writeValueAsString(it) } ?: ""
    }

    companion object {
        private val logger = LoggerFactory.getLogger(RpcHttpClient::class.java)

        private val objectMapper = ObjectMapper()

        private val httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build()
    }
}
