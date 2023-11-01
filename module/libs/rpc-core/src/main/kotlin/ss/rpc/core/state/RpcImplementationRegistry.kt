package ss.rpc.core.state

class RpcImplementationRegistry {

    private val implementationsMap: HashMap<Class<*>, Any> = HashMap()

    fun register(implementations: Map<Class<*>, Any>) {
        implementationsMap.clear()
        implementationsMap.putAll(implementations)
    }

    fun getBean(clazz: Class<*>): Any? {
        return implementationsMap.get(clazz)
    }

    companion object {

        @Volatile
        private var instance: RpcImplementationRegistry? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: RpcImplementationRegistry().also { instance = it }
        }
    }
}