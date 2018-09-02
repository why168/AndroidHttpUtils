package io.github.why168.httpcacheandroid

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import io.github.why168.httpcacheandroid.db.AppDatabase
import kotlin.properties.Delegates

/**
 *
 * Application
 *
 * @author Edwin.Wu edwin.wu05@gmail.com
 * @version 2018/8/30 下午10:45
 * @since JDK1.8
 */
class MyApplication : Application() {

    companion object {
        var context: Context by Delegates.notNull()
        var mHandler: Handler by Delegates.notNull()
        fun isMainThread() = Looper.getMainLooper() == Looper.myLooper()
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        mHandler = Handler(Looper.getMainLooper())

        AppDatabase.getInstance()
    }
}