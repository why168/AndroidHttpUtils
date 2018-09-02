package io.github.why168.httpcacheandroid.http.call

import io.github.why168.httpcacheandroid.db.AppDatabase
import io.github.why168.httpcacheandroid.extensions.MD5
import io.github.why168.httpcacheandroid.http.callback.BaseCallback
import retrofit2.Call
import java.io.IOException
import java.util.concurrent.Executor
import io.github.why168.httpcacheandroid.http.DispatcherUtils

/**
 *
 *
 *
 * @author Edwin.Wu
 * @version 2018/8/30 下午7:14
 * @since JDK1.8
 */
class MyCallAdapter<T>(private val call: Call<T>,
                       private val callbackExecutor: Executor) : MyCall<T> {

    override fun cancel() {
        call.cancel()
    }

    override fun enqueue(callback: BaseCallback<T>) {
        callbackExecutor.execute {
            val urlKey = call.request().url().toString().MD5()

            // 首先-判断是否开启缓存
            if (callback.isOpenCache()) {
                // 获取缓存
                callback.getLocalCache(urlKey, null)?.let {
                    // 成功获取刷新
                    DispatcherUtils.runOnUiThread(Runnable {
                        callback.successCache(it)
                    })
                }
            }

            // 再去请求网络
            try {
                val response = call.execute()
                val code = response.code()
                when (code) {
                    in 200..299 -> {
                        // 判断Head头-ETag是否有更新-如果与本地一致则不在回调刷新
                        val eTag = response.headers().get("ETag").toString()

                        val cacheDao = AppDatabase.getInstance().cacheDao()




                        // 判断是否开启缓存
                        if (callback.isOpenCache()) {

                            // 判断缓存是是否过期
                            if (cacheDao.checkByKeyAndETag(urlKey, eTag) == null) {
                                DispatcherUtils.runOnUiThread(Runnable {
                                    callback.success(response)
                                })
                            }

                            // 缓存更新
                            callback.cache(urlKey, eTag, response.body()!!)
                        }else{
                            DispatcherUtils.runOnUiThread(Runnable {
                                callback.success(response)
                            })
                        }
                    }
                    401 -> {
                        DispatcherUtils.runOnUiThread(Runnable {
                            callback.unauthenticated(response)
                        })
                    }
                    in 400..499 -> {
                        DispatcherUtils.runOnUiThread(Runnable {
                            callback.clientError(response)
                        })
                    }
                    in 500..599 -> {
                        DispatcherUtils.runOnUiThread(Runnable {
                            callback.serverError(response)
                        })
                    }
                    else -> {
                        DispatcherUtils.runOnUiThread(Runnable {
                            callback.unexpectedError(RuntimeException("Unexpected response $response"))
                        })
                    }
                }
            } catch (e: Exception) {
                DispatcherUtils.runOnUiThread(Runnable {
                    if (e is IOException) {
                        callback.networkError(e)
                    } else {
                        callback.unexpectedError(e)
                    }
                })
            }
        }
    }

    override fun clone(): MyCall<T> {
        return MyCallAdapter(call.clone(), callbackExecutor)
    }
}
