package ss.rpc.discovery

import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import ss.rpc.core.RpcCallSignature
import ss.rpc.core.RpcService
import ss.rpc.discovery.core.DISCOVERY_SERVER_HOST
import ss.rpc.discovery.core.DISCOVERY_SERVER_PORT
import ss.rpc.discovery.core.DISCOVERY_SERVER_SCHEMA
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import java.time.Duration

@Configuration
open class DiscoveryBootstrap(
    private val applicationContext: ApplicationContext
) {

    private val httpClient = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .connectTimeout(Duration.ofSeconds(10))
        .build();

    @EventListener(ContextRefreshedEvent::class)
    fun onAppStartup() {
        val rpcServices = findRpcServices()
        val rpcCallSignatures = prepareRpcCallSignatures(rpcServices)
        discoverRpcCalls(rpcCallSignatures)
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

    private fun discoverRpcCalls(rpcCallSignatures: List<RpcCallSignature>) {
        val uri = URI(
            String.format(
                "%s://%s:%s/discovery",
                DISCOVERY_SERVER_SCHEMA,
                DISCOVERY_SERVER_HOST,
                DISCOVERY_SERVER_PORT
            )
        )
        val payload = "[" + rpcCallSignatures.joinToString(",") { """"$it"""" } + "]"
        println(payload)
        println(uri)
        val statusCode = httpClient.send(
            HttpRequest.newBuilder().uri(
                uri
            ).header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(payload)).build(),
            BodyHandlers.ofString()
        ).body()
        println(statusCode)
    }
}
