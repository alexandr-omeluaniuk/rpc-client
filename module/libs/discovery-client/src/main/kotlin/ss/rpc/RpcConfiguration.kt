package ss.rpc

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.core.type.classreading.CachingMetadataReaderFactory
import org.springframework.util.ClassUtils
import ss.rpc.core.RpcService
import ss.rpc.proxy.RpcClientProxy
import java.lang.reflect.Proxy

@Configuration
class RpcConfiguration() : BeanDefinitionRegistryPostProcessor {

    private val logger = LoggerFactory.getLogger(RpcConfiguration::class.java)

    override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {

    }

    override fun postProcessBeanDefinitionRegistry(registry: BeanDefinitionRegistry) {
        val rpcClients = scanForRpcClients()
        rpcClients.forEach { registerRpcClientAsBean(it, registry) }
    }

    private fun scanForRpcClients(): Set<Class<Any>> {
        val classPackage = javaClass.packageName
        val packageSearchPath = "classpath*:${classPackage.replace(".", "/")}/**/*.class"
        val resources = PathMatchingResourcePatternResolver().getResources(packageSearchPath).filter {
            !(it.filename?.contains(ClassUtils.CGLIB_CLASS_SEPARATOR) ?: true)
        }
        logger.debug("[${resources.size}] Spring resources detected by search path [$packageSearchPath]")
        val metadataReaderFactory = CachingMetadataReaderFactory()
        val classes = resources.map {
            Class.forName(metadataReaderFactory.getMetadataReader(it).classMetadata.className) as Class<Any>
        }
        val rpcServices = classes.filter { cl ->
            cl.isInterface && cl.getAnnotation(RpcService::class.java) != null
        }
        logger.debug("Total RpcService interfaces detected [${rpcServices.size}]")
        val implementedInterfaces = classes.mapNotNull {  cl ->
            when (cl.isInterface) {
                true -> null
                false -> cl.interfaces.asList()
            }
        }.flatten()
        return rpcServices.filter { it -> !implementedInterfaces.contains(it) }.toSet().also {
            it.forEach { logger.debug("RpcService client detected [$it]") }
        }
    }

    private fun registerRpcClientAsBean(rpcClientInterface: Class<Any>, registry: BeanDefinitionRegistry) {
        val proxy = Proxy.newProxyInstance(
            rpcClientInterface.getClassLoader(),
            arrayOf(rpcClientInterface),
            RpcClientProxy()
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