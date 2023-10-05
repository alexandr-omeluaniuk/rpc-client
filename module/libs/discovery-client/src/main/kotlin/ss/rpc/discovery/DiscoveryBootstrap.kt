package ss.rpc.discovery

import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import ss.rpc.core.RpcCallSignature
import ss.rpc.core.RpcService

@Configuration
open class DiscoveryBootstrap(
    private val applicationContext: ApplicationContext
) {

    @EventListener(ContextRefreshedEvent::class)
    fun onAppStartup() {
        val rpcServices = findRpcServices()
        val rpcCallSignatures = prepareRpcCallSignatures(rpcServices)
        rpcCallSignatures.forEach {
            println(it.toString())
        }
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
}
