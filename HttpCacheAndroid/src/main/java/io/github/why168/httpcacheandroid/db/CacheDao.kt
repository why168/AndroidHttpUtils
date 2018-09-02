package io.github.why168.httpcacheandroid.db

import android.arch.persistence.room.*

import io.github.why168.httpcacheandroid.model.Cache

/**
 *
 * @author Edwin.Wu edwin.wu05@gmail.com
 * @version 2018/8/30 下午10:49
 * @since JDK1.8
 */
@Dao
interface CacheDao {

    @Query("SELECT * FROM Cache")
    fun loadAll(): List<Cache>

    @Query("SELECT * FROM Cache WHERE uid=:userIds")
    fun loadAllByUserId(vararg userIds: Int): List<Cache>

    @Query("SELECT * FROM Cache WHERE url=:cacheKey")
    fun checkByKey(cacheKey: String): Cache?

    @Query("SELECT * FROM Cache WHERE url=:cacheKey AND eTag=:cacheETag")
    fun checkByKeyAndETag(cacheKey: String, cacheETag: String): Cache?

    @Update
    fun update(caches: Cache): Int

    @Insert
    fun insert(caches: Cache)

    @Insert
    fun insertAll(vararg caches: Cache)

    @Delete
    fun delete(caches: Cache)

}