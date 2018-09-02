package io.github.why168.httpcacheandroid.http.call

import io.github.why168.httpcacheandroid.http.callback.BaseCallback

/**
 *
 * Call
 *
 * @author Edwin.Wu
 * @version 2018/8/31 下午12:19
 * @since JDK1.8
 */
interface MyCall<T> {

    public fun cancel()

    public fun enqueue(callback: BaseCallback<T>)

    public fun clone(): MyCall<T>
}