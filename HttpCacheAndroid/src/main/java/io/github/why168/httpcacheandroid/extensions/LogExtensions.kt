package io.github.why168.httpcacheandroid.extensions

import android.util.Log
import io.github.why168.httpcacheandroid.BuildConfig
import io.github.why168.httpcacheandroid.Constants

/**
 *
 * 日志打印
 *
 * @author Edwin.Wu edwin.wu05@gmail.com
 * @version 2018/8/31 下午3:00
 * @since JDK1.8
 */

fun logByError(msg: String) {
    if (BuildConfig.DEBUG)
        Log.e(Constants.TAG, msg)
}

fun logByInfo(msg: String) {
    if (BuildConfig.DEBUG)
        Log.i(Constants.TAG, msg)
}

fun logByDebug(msg: String) {
    if (BuildConfig.DEBUG)
        Log.d(Constants.TAG, msg)
}