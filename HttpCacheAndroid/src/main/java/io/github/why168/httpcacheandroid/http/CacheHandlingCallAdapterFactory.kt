package io.github.why168.httpcacheandroid.http


import io.github.why168.httpcacheandroid.http.call.MyCall
import io.github.why168.httpcacheandroid.http.call.MyCallAdapter
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.concurrent.Executor

/**
 *
 * CacheHandling
 *
 * CallAdapter
 *
 * @author Edwin.Wu edwin.wu05@gmail.com
 * @version 2018/8/31 下午2:53
 * @since JDK1.8
 */
class CacheHandlingCallAdapterFactory : CallAdapter.Factory() {

    companion object {
        fun create(): CacheHandlingCallAdapterFactory {
            return CacheHandlingCallAdapterFactory()
        }
    }

    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {

        // 判断反射
        if (getRawType(returnType) != MyCall::class.java) {
            return null
        }

        if (returnType !is ParameterizedType) {
            throw IllegalStateException("MyCall must have generic type (e.g., MyCall<ResponseBody>)")
        }

        val responseType = getParameterUpperBound(0, returnType)


//        val callbackExecutor = retrofit.callbackExecutor()

        val callbackExecutor = DispatcherUtils.dispatcherAsync.executorService()

        return ErrorHandlingCallAdapter<Any>(responseType, callbackExecutor)
    }


    private class ErrorHandlingCallAdapter<R> internal constructor(private val responseType: Type, private val callbackExecutor: Executor) : CallAdapter<R, MyCall<R>> {

        override fun responseType(): Type {
            return responseType
        }

        override fun adapt(call: Call<R>): MyCall<R> {
            return MyCallAdapter(call, callbackExecutor)
        }
    }
}


