package ss.rpc.discovery.rest

import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ss.rpc.discovery.service.DiscoveryService

@RestController
@RequestMapping("/discovery")
class DiscoveryController(
    private val discoveryService: DiscoveryService
) {

    @PutMapping("/register/{name}")
    fun registerMicroservice(
        @PathVariable("name") rpcCallName: String,
        request: HttpServletRequest
    ) {
        discoveryService.registerRpcCall(rpcCallName, request.remoteHost, request.remotePort)
    }
}