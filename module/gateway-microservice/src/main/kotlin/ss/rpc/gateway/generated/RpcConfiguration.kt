package ss.rpc.gateway.generated

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.context.annotation.Configuration
import ss.rpc.boundary.CalculatorService
import java.lang.reflect.Proxy

@Configuration
class RpcConfiguration() : BeanDefinitionRegistryPostProcessor {
    override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {

    }

    override fun postProcessBeanDefinitionRegistry(registry: BeanDefinitionRegistry) {
        val builder = BeanDefinitionBuilder.genericBeanDefinition(
            CalculatorService::class.java
        ) {
            Proxy.newProxyInstance(
                CalculatorService::class.java.getClassLoader(),
                arrayOf(CalculatorService::class.java),
                CalculatorServiceProxy()
            ) as CalculatorService
        }.setLazyInit(true)
        registry.registerBeanDefinition(
            "calculatorService",
            builder.beanDefinition
        )
    }
}