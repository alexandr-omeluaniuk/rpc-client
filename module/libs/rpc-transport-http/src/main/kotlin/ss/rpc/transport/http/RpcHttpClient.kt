package ss.rpc.transport.http

import ss.rpc.core.api.RpcClientProxy
import java.lang.reflect.Method

class RpcHttpClient : RpcClientProxy {
    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any {
        println(method)
        println(args)
        println("RPC call over HTTP")
        return 0
    }
}
