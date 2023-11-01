package ss.rpc

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import ss.rpc.core.RpcCallSignature
import ss.rpc.core.RpcService
import ss.rpc.core.state.RoutingTable
import ss.rpc.core.state.RpcImplementationRegistry
import ss.rpc.core.state.RpcRoute
import ss.rpc.discovery.core.DISCOVERY_SERVER_HOST
import ss.rpc.discovery.core.DISCOVERY_SERVER_PORT
import ss.rpc.discovery.core.DISCOVERY_SERVER_SCHEMA
import ss.rpc.discovery.core.RpcRegistrationInfo
import java.net.ConnectException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import java.time.Duration
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

@Configuration
open class RpcDiscoveryBootstrap(
    private val applicationContext: ApplicationContext,
    @Value("\${server.port}")
    private val port: Int,
    private val objectMapper: ObjectMapper
) {

    private val httpClient = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .connectTimeout(Duration.ofSeconds(10))
        .build()

    @EventListener(ContextRefreshedEvent::class)
    fun onAppStartup() {
        val rpcImplementations = findRpcImplementations()
        RpcImplementationRegistry.getInstance().register(rpcImplementations)
        val rpcCallSignatures = prepareRpcCallSignatures(rpcImplementations)
        discoverRpcCallsPeriodically(rpcCallSignatures)
    }

    private fun findRpcImplementations(): Map<Class<*>, Any> {
        val rpcServices = HashMap<Class<*>, Any>()
        val rpcImplementations = RpcServicesScanner.getInstance().getRpcImplementations()
        applicationContext.beanDefinitionNames.forEach { beanName ->
            val bean = applicationContext.getBean(beanName)
            val beanClass = bean.javaClass
            beanClass.interfaces.forEach { beanContract ->
                if (
                    beanContract.getAnnotation(RpcService::class.java) != null &&
                    rpcImplementations.contains(beanContract)
                ) {
                    rpcServices[beanContract] = bean
                }
            }
        }
        return rpcServices
    }

    private fun prepareRpcCallSignatures(rpcImplementations: Map<Class<*>, Any>): List<RpcCallSignature> =
        rpcImplementations.keys.map { rpcService ->
            rpcService.declaredMethods.map {
                RpcCallSignature(it)
            }
        }.flatten()

    private fun discoverRpcCallsPeriodically(rpcCallSignatures: List<RpcCallSignature>) {
        thread(start = true) {
            while (true) {
                discoverRpcCalls(rpcCallSignatures)
                Thread.sleep(TimeUnit.SECONDS.toMillis(30))
            }
        }
    }

    private fun discoverRpcCalls(rpcCallSignatures: List<RpcCallSignature>) {
        val uri = URI(
            String.format(
                "%s://%s:%s/discovery",
                DISCOVERY_SERVER_SCHEMA,
                DISCOVERY_SERVER_HOST,
                DISCOVERY_SERVER_PORT
            )
        )
        val payload = RpcRegistrationInfo(
            port = port,
            signatures = rpcCallSignatures.map { it.toString() }
        )
        val payloadString = objectMapper.writeValueAsString(payload)
        try {
            val routingTableString = httpClient.send(
                HttpRequest.newBuilder().uri(
                    uri
                ).header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(payloadString)).build(),
                BodyHandlers.ofString()
            ).body()
            val listRoutes: List<RpcRoute> = objectMapper.readValue<List<RpcRoute>>(routingTableString)
            RoutingTable.getInstance().update(listRoutes)
        } catch (e: ConnectException) {

        }
    }
}
