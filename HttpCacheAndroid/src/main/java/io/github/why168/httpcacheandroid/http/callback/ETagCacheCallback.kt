package io.github.why168.httpcacheandroid.http.callback

import com.google.gson.Gson
import io.github.why168.httpcacheandroid.DateUtils
import io.github.why168.httpcacheandroid.db.AppDatabase
import io.github.why168.httpcacheandroid.extensions.logByInfo
import io.github.why168.httpcacheandroid.model.Cache
import java.lang.reflect.ParameterizedType

/**
 *
 * 自定义缓存，产品需要根据Head头里面的ETag值进行比较缓存
 *
 *
 * @author Edwin.Wu edwin.wu05@gmail.com
 * @version 2018/8/31 下午3:06
 * @since JDK1.8
 */
abstract class ETagCacheCallback<T> : CacheCallback<T>() {

    /*

        GET /home.json HTTP/1.1
        Host: 192.168.0.1
        Connection: Keep-Alive
        Accept-Encoding: gzip
        User-Agent: okhttp/3.0.1

        HTTP/1.1 200 OK
        Server: Apache
        Last-Modified: Mon, 30 Jun 2014 04:44:43 GMT
        ETag: "1205-4fd06515d9572"-----------------------通过ETag判断是否刷新、缓存
        Content-Type: image/png
        Content-Length: 4613
        Accept-Ranges: bytes
        Date: Thu, 04 Feb 2016 13:30:05 GMT
        X-Varnish: 3684240013 3683622711
        Age: 1313
        Via: 1.1 varnish
        Connection: keep-alive
    */


    override fun isOpenCache(): Boolean = true

    override fun checkUpdateCache(key: String, eTag: String): Boolean {
        val cacheDao = AppDatabase.getInstance().cacheDao()
        val cacheKey = cacheDao.checkByKeyAndETag(key, eTag)
        return cacheKey == null
    }

    override fun cache(key: String, eTag: String, value: T) {
        val data = Gson().toJson(value).toString()

        val cacheDao = AppDatabase.getInstance().cacheDao()
        val cacheKey = cacheDao.checkByKeyAndETag(key, eTag)
        if (cacheKey == null) {
            // 直接写入缓存
            logByInfo("cache insert 写入缓存 --> currentThread = ${Thread.currentThread().name}")
            cacheDao.insert(Cache(key = key, value = data, eTag = eTag))
        } else {
            logByInfo("cache update 更新缓存 --> currentThread = ${Thread.currentThread().name}")
            cacheDao.update(Cache(uid = cacheKey.uid, key = cacheKey.key, value = data, eTag = eTag, time = DateUtils.getLoactTime()))
        }
    }

    override fun getLocalCache(key: String, eTag: String?): T? {
        // 泛类型解析、具体我也不懂什么意思、没来及去看
        val type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
        val cacheDao = AppDatabase.getInstance().cacheDao()

        return if (eTag == null) {
            val checkByKey = cacheDao.checkByKey(key)
            if (checkByKey == null) {
                null
            } else {
                Gson().fromJson<T>(checkByKey.value, type)
            }
        } else {
            val checkByKey = cacheDao.checkByKeyAndETag(key, eTag)
            if (checkByKey == null) {
                null
            } else {
                Gson().fromJson<T>(checkByKey.value, type)
            }
        }
    }
}