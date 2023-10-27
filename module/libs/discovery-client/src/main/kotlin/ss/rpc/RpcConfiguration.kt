package ss.rpc

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.context.annotation.Configuration
import ss.rpc.core.api.RpcClientProxy
import ss.rpc.util.RpcServicesScanner
import java.lang.reflect.Proxy
import java.util.*

@Configuration
open class RpcConfiguration() : BeanDefinitionRegistryPostProcessor {

    override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {

    }

    override fun postProcessBeanDefinitionRegistry(registry: BeanDefinitionRegistry) {
        val rpcClients = RpcServicesScanner().findClients()
        rpcClients.forEach { registerRpcClientAsBean(it, registry) }
    }

    private fun registerRpcClientAsBean(rpcClientInterface: Class<Any>, registry: BeanDefinitionRegistry) {
        val rpcTransportLayer = ServiceLoader.load(RpcClientProxy::class.java).findFirst().get()
        val proxy = Proxy.newProxyInstance(
            rpcClientInterface.getClassLoader(),
            arrayOf(rpcClientInterface),
            rpcTransportLayer
        )
        val builder = BeanDefinitionBuilder.genericBeanDefinition(
            rpcClientInterface
        ) { proxy }.setLazyInit(true)
        registry.registerBeanDefinition(
            rpcClientInterface.simpleName,
            builder.beanDefinition
        )
    }
}