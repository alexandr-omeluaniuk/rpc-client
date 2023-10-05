package ss.discovery.core

import org.springframework.context.annotation.Configuration
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener

@Configuration
open class DiscoveryBootstrap {

    @EventListener(ContextRefreshedEvent::class)
    fun onAppStartup() {
        println("STARTUP WORKS")
    }
}