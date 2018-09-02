package io.github.why168.httpcacheandroid.http

import io.github.why168.httpcacheandroid.BuildConfig
import io.github.why168.httpcacheandroid.Constants
import io.github.why168.httpcacheandroid.http.service.HttpBinService
import io.github.why168.httpcacheandroid.http.service.HttpGithubService
import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 *
 *
 * @author Edwin.Wu edwin.wu05@gmail.com
 * @version 2018/8/31 下午2:38
 * @since JDK1.8
 */
class RetrofitUtils {

    companion object {

        private var retrofit: Retrofit.Builder? = null
            get() {
                if (field == null) {
                    val builder = OkHttpClient.Builder()
                    val logging = HttpLoggingInterceptor()

                    if (BuildConfig.DEBUG) {
                        logging.level = HttpLoggingInterceptor.Level.BODY
                    } else {
                        logging.level = HttpLoggingInterceptor.Level.NONE
                    }

                    val client = builder
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .writeTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .connectionPool(ConnectionPool(5, 5, TimeUnit.MINUTES))
//                            .addInterceptor(HeadInterceptor()) // addInterceptor ——> response被调用一次
                            .addNetworkInterceptor(logging) // addNetworkInterceptor ——> request和response是分别被调用一次
//                            .addNetworkInterceptor(HttpCacheInterceptor())
//                            .cache(Cache(File(DApps.context.cacheDir, "responses"), Constants.CACHE_SIZE_BYTES))
//                            .cookieJar(SimpleCookieJar())
                            .retryOnConnectionFailure(true) // 连接失败后是否重新连接
                            .followRedirects(true) // 302重定向
//                            .sslSocketFactory(SSLFactory.initSSLSocketFactory(), SSLFactory.initTrustManager())
                            .build()


                    field = Retrofit
                            .Builder()
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .addCallAdapterFactory(CacheHandlingCallAdapterFactory.create())
                            .client(client)
                }
                return field!!
            }

        internal var httpBinService: HttpBinService? = null
            get() {
                if (field == null) {
                    field = retrofit?.baseUrl(Constants.BIN_API)?.build()?.create(HttpBinService::class.java)
                }
                return field
            }

        internal var httpGithubService: HttpGithubService? = null
            get() {
                if (field == null) {
                    field = retrofit?.baseUrl(Constants.GITHUB_API)?.build()?.create(HttpGithubService::class.java)
                }
                return field
            }

    }

}