package ss.rpc.proxy

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

class RpcClientProxy : InvocationHandler {
    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any {
        println(method)
        println(args)
        println("RPC call")
        return 0
    }
}