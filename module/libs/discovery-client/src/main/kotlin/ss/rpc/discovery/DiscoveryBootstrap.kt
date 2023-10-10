package ss.rpc.discovery

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import ss.rpc.core.RpcCallSignature
import ss.rpc.core.RpcService
import ss.rpc.discovery.core.*
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

@Configuration
open class DiscoveryBootstrap(
    private val applicationContext: ApplicationContext,
    @Value("\${server.port}")
    private val port: Int,
    private val objectMapper: ObjectMapper
) {

    private val httpClient = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .connectTimeout(Duration.ofSeconds(10))
        .build();

    private val routingTable: MutableMap<String, RpcRoute> = ConcurrentHashMap()

    @EventListener(ContextRefreshedEvent::class)
    fun onAppStartup() {
        val rpcServices = findRpcServices()
        val rpcCallSignatures = prepareRpcCallSignatures(rpcServices)
        discoverRpcCallsPeriodically(rpcCallSignatures)
    }

    private fun findRpcServices(): Map<Class<*>, Any> {
        val rpcServices = HashMap<Class<*>, Any>()
        applicationContext.beanDefinitionNames.forEach { beanName ->
            val bean = applicationContext.getBean(beanName)
            val beanClass = bean.javaClass
            beanClass.interfaces.forEach { beanContract ->
                if (beanContract.getAnnotation(RpcService::class.java) != null) {
                    rpcServices[beanContract] = bean
                }
            }
        }
        return rpcServices
    }

    private fun prepareRpcCallSignatures(rpcServices: Map<Class<*>, Any>): List<RpcCallSignature> =
        rpcServices.keys.map { rpcService ->
            rpcService.declaredMethods.map {
                RpcCallSignature(rpcService, it)
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
        val routingTableString = httpClient.send(
            HttpRequest.newBuilder().uri(
                uri
            ).header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(payloadString)).build(),
            BodyHandlers.ofString()
        ).body()
        val listRoutes: List<RpcRoute> = objectMapper.readValue<List<RpcRoute>>(routingTableString)
        routingTable.clear()
        routingTable.putAll(listRoutes.associateBy(RpcRoute::rpcCallName))
        println(routingTable)
    }
}
