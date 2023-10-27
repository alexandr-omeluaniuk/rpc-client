package ss.rpc.discovery.rest

import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ss.rpc.core.state.RpcRoute
import ss.rpc.discovery.core.RpcRegistrationInfo
import ss.rpc.discovery.service.DiscoveryService

@RestController
@RequestMapping("/discovery")
class DiscoveryController(
    private val discoveryService: DiscoveryService
) {

    @PutMapping
    fun registerRpcCalls(
        @RequestBody registrationInfo: RpcRegistrationInfo,
        request: HttpServletRequest
    ): List<RpcRoute> =
        discoveryService.registerRpcCall(registrationInfo, request.remoteHost)

}