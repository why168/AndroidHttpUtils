package io.github.why168.httpcacheandroid.http.callback

import io.github.why168.httpcacheandroid.extensions.logByError
import retrofit2.Response
import java.io.IOException

/**
 *
 * 缓存Callback
 *
 * @author Edwin.Wu
 * @version 2018/8/30 下午7:53
 * @since JDK1.8
 */
abstract class CacheCallback<T> : BaseCallback<T> {

    override fun unauthenticated(response: Response<*>) {
        logByError("UNAUTHENTICATED")
    }

    override fun clientError(response: Response<*>) {
        logByError("CLIENT ERROR " + response.code() + " " + response.message())
    }

    override fun serverError(response: Response<*>) {
        logByError("SERVER ERROR " + response.code() + " " + response.message())
    }

    override fun networkError(e: IOException) {
        logByError("NETWORK ERROR " + e.message)
    }

    override fun unexpectedError(t: Throwable) {
        logByError("FATAL ERROR " + t.message)
    }
}