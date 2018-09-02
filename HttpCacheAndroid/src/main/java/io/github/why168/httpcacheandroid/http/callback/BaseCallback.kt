package io.github.why168.httpcacheandroid.http.callback

import retrofit2.Response
import java.io.IOException

/**
 *
 * BaseCallback
 *
 * @author Edwin.Wu
 * @version 2018/8/30 下午7:13
 * @since JDK1.8
 */

interface BaseCallback<T> {

    /**
     * 是不开启缓存
     * OtherThread
     */
    fun isOpenCache(): Boolean


    /**
     * 检测缓存是否更新
     * OtherThread
     */
    fun checkUpdateCache(key: String, eTag: String): Boolean

    /**
     *
     * 缓存
     * OtherThread
     */
    fun cache(key: String, eTag: String, value: T)

    /**
     * 获取本地缓存
     * OtherThread
     */
    fun getLocalCache(key: String, eTag: String?): T?

    /**
     * 缓存刷新成功
     * MainThread
     */
    fun successCache(t: T)

    /**
     * Called for [200, 300) responses.
     * MainThread
     */
    fun success(response: Response<T>)

    /**
     * Called for 401 responses.
     * MainThread
     */
    fun unauthenticated(response: Response<*>)

    /**
     * Called for [400, 500) responses, except 401.
     * MainThread
     */
    fun clientError(response: Response<*>)

    /**
     * Called for [500, 600) response.
     * MainThread
     */
    fun serverError(response: Response<*>)

    /**
     * Called for network errors while making the call.
     * MainThread
     */
    fun networkError(e: IOException)

    /**
     * Called for unexpected errors while making the call.
     * MainThread
     */
    fun unexpectedError(t: Throwable)
}