package ss.rpc

import org.slf4j.LoggerFactory
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.core.type.classreading.CachingMetadataReaderFactory
import org.springframework.util.ClassUtils
import ss.rpc.core.RpcService

class RpcServicesScanner {

    private val logger = LoggerFactory.getLogger(RpcServicesScanner::class.java)

    fun findClients(): Set<Class<Any>> {
        val resources = searchResources()
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
        return rpcServices.filter { !implementedInterfaces.contains(it) }.toSet().also {
            it.forEach { logger.debug("RpcService client detected [$it]") }
        }
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
}