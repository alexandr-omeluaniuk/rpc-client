package ss.rpc.transport.http

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ss.rpc.transport.http.model.RpcCall

@RestController
@RequestMapping(RPC_ENDPOINT)
class RpcHttpServer {

    private val logger = LoggerFactory.getLogger(RpcHttpServer::class.java)

    @PostMapping
    fun rpcCall(
        @RequestBody payload: RpcCall
    ): String? {
        logger.debug("Accept payload [$payload]")
        return ""
    }
}