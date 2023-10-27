package ss.rpc

import org.slf4j.LoggerFactory
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.core.type.classreading.CachingMetadataReaderFactory
import org.springframework.util.ClassUtils
import ss.rpc.core.RpcService

class RpcServicesScanner {

    private val logger = LoggerFactory.getLogger(RpcServicesScanner::class.java)

    private val rpcClients: HashSet<Class<Any>> = HashSet()
    private val rpcImplementations: HashSet<Class<Any>> = HashSet()

    companion object {

        @Volatile
        private var instance: RpcServicesScanner? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: RpcServicesScanner().also { instance = it }.also { it.scan() }
        }
    }

    fun getRpcClients(): Set<Class<Any>> = rpcClients

    fun getRpcImplementations(): Set<Class<Any>> = rpcImplementations

    private fun scan() {
        val resources = searchResources()
        val metadataReaderFactory = CachingMetadataReaderFactory()
        val classes = resources.map {
            Class.forName(metadataReaderFactory.getMetadataReader(it).classMetadata.className) as Class<Any>
        }
        val rpcServices = classes.filter { cl ->
            cl.isInterface && cl.getAnnotation(RpcService::class.java) != null
        }
        logger.debug("Total RpcService interfaces detected [${rpcServices.size}]")
        val implementedInterfaces = findImplementedInterfaces(classes)
        findRpcClients(rpcServices, implementedInterfaces)
        findRpcImplementations(rpcServices, implementedInterfaces)
    }

    private fun searchResources(): List<Resource> {
        val classPackage = javaClass.packageName
        val packageSearchPath = "classpath*:${classPackage.replace(".", "/")}/**/*.class"
        return PathMatchingResourcePatternResolver().getResources(packageSearchPath).filter {
            !(it.filename?.contains(ClassUtils.CGLIB_CLASS_SEPARATOR) ?: true)
        }.also {
            logger.debug("[${it.size}] Spring resources detected by search path [$packageSearchPath]")
        }
    }

    private fun findImplementedInterfaces(classes: List<Class<Any>>): List<Class<*>> =
        classes.mapNotNull {  cl ->
            when (cl.isInterface) {
                true -> null
                false -> cl.interfaces.asList()
            }
        }.flatten()

    private fun findRpcClients(rpcServices: List<Class<Any>>, implementedInterfaces: List<Class<*>>) {
        val clients = rpcServices.filter { !implementedInterfaces.contains(it) }.also {
            it.forEach { logger.debug("RpcService client detected [$it]") }
        }
        rpcClients.addAll(clients)
    }

    private fun findRpcImplementations(rpcServices: List<Class<Any>>, implementedInterfaces: List<Class<*>>) {
        val implementations = rpcServices.filter { implementedInterfaces.contains(it) }.also {
            it.forEach { logger.debug("RpcService implementation detected [$it]") }
        }
        rpcImplementations.addAll(implementations)
    }
}