package ss.rpc.discovery

import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import ss.rpc.core.RpcService

@Configuration
open class DiscoveryBootstrap(
    private val applicationContext: ApplicationContext
) {

    @EventListener(ContextRefreshedEvent::class)
    fun onAppStartup() {
        val rpcServices = findRpcServices()
    }

    private fun findRpcServices(): Map<Class<Any>, Any> {
        val rpcServices = HashMap<Class<Any>, Any>()
        applicationContext.beanDefinitionNames.forEach { beanName ->
            val bean = applicationContext.getBean(beanName)
            val beanClass = bean.javaClass
            beanClass.interfaces.forEach { beanContract ->
                if (beanContract.getAnnotation(RpcService::class.java) != null) {
                    rpcServices[beanClass] = bean
                }
            }
        }
        return rpcServices
    }
}