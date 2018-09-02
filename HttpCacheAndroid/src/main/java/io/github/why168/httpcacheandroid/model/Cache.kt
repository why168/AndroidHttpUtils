package io.github.why168.httpcacheandroid.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import android.text.format.DateUtils
import org.jetbrains.anko.db.UNIQUE
import java.util.*

/**
 * @author Edwin.Wu edwin.wu05@gmail.com
 * @version 2018/8/30 下午10:48
 * @since JDK1.8
 *
 */
@Entity(tableName = "cache", indices = [Index(value = ["eTag"]), Index(value = ["url"])])
data class Cache(
        @PrimaryKey(autoGenerate = true)
        var uid: Int = 0,

        @ColumnInfo(name = "url")
        var key: String = "",

        @ColumnInfo(name = "value")
        var value: String = "",

        @ColumnInfo(name = "eTag")
        var eTag: String = "",

        @ColumnInfo(name = "time")
        var time: String = io.github.why168.httpcacheandroid.DateUtils.getLoactTime()) {

    override fun toString(): String {
        return "Cache(uid=$uid, key='$key', value='$value', eTag='$eTag', time='$time')"
    }
}