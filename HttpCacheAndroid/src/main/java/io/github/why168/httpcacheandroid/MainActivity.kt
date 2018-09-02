package io.github.why168.httpcacheandroid

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import io.github.why168.httpcacheandroid.extensions.logByInfo
import io.github.why168.httpcacheandroid.http.RetrofitUtils
import io.github.why168.httpcacheandroid.http.callback.BaseCallback
import io.github.why168.httpcacheandroid.http.callback.ETagCacheCallback
import io.github.why168.httpcacheandroid.model.github.GithubModel
import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 *
 * MainActivity
 *
 * @author Edwin.Wu edwin.wu05@gmail.com
 * @version 2018/8/31 下午2:54
 * @since JDK1.8
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initAll()

    }

    private fun initAll() {
        // 可以查看手机里面的数据库数据-开启
        SQLiteStudioService.instance().start(this)
    }


    public fun requestCacheHttp(view: View) {
        requestHttp()
    }


    private fun requestHttp() {
        val getGitHub = RetrofitUtils.httpGithubService?.getGitHub()
        getGitHub?.enqueue(object : ETagCacheCallback<GithubModel>() {
            override fun isOpenCache(): Boolean {
                return false
            }

            override fun successCache(t: GithubModel) {
                logByInfo("successCache --> currentThread = ${Thread.currentThread().name}")
            }

            override fun success(response: Response<GithubModel>) {
                logByInfo("success --> currentThread = ${Thread.currentThread().name}")
            }
        })

        getGitHub?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 可以查看手机里面的数据库数据-关闭
        SQLiteStudioService.instance().stop()
    }

}
