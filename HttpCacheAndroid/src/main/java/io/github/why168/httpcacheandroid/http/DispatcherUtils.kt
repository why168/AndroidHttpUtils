package io.github.why168.httpcacheandroid.http

import android.os.Handler
import android.os.Looper
import okhttp3.Dispatcher

/**
 *
 *
 * @author Edwin.Wu edwin.wu05@gmail.com
 * @version 2018/8/31 下午5:59
 * @since JDK1.8
 */
class DispatcherUtils {

    companion object {
//        private val threadPool = Executors.newCachedThreadPool() as ThreadPoolExecutor

        var dispatcherAsync: Dispatcher = Dispatcher() // 调度处理异步

        private val handler = Handler(Looper.getMainLooper()) // 调度处理UI线程

        @Synchronized
        fun async(command: Runnable) {
            dispatcherAsync.executorService().execute(command)
        }

        @Synchronized
        fun runOnUiThread(command: Runnable) {
            handler.post(command)
        }

    }

}