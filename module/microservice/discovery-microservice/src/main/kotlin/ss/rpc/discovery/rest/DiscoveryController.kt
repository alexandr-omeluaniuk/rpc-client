package ss.rpc.discovery.rest

import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.*
import ss.rpc.discovery.service.DiscoveryService

@RestController
@RequestMapping("/discovery")
class DiscoveryController(
    private val discoveryService: DiscoveryService
) {

    @PutMapping
    fun registerRpcCalls(
        @RequestBody rpcCallSignatures: List<String>,
        request: HttpServletRequest
    ) {
        discoveryService.registerRpcCall(rpcCallSignatures, request.remoteHost, request.remotePort)
    }

    @GetMapping
    fun healthCheck(): String {
        return "OK"
    }
}