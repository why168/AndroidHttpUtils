package io.github.why168.httpcacheandroid.db

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import io.github.why168.httpcacheandroid.Constants
import io.github.why168.httpcacheandroid.MyApplication

import io.github.why168.httpcacheandroid.model.Cache

/**
 * @author Edwin.Wu edwin.wu05@gmail.com
 * @version 2018/8/30 下午10:49
 * @since JDK1.8
 */
@Database(entities = [Cache::class], version = 1, exportSchema = true)
public abstract class AppDatabase : RoomDatabase() {

    abstract fun cacheDao(): CacheDao

    companion object {
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(): AppDatabase {
            if (instance == null) {
                synchronized(AppDatabase::class.java) {
                    if (instance == null) {
                        instance = Room.databaseBuilder<AppDatabase>(MyApplication.context,
                                AppDatabase::class.java,
                                Constants.DB_NAME)
                                .fallbackToDestructiveMigration()
                                .allowMainThreadQueries()
                                .addCallback(object : android.arch.persistence.room.RoomDatabase.Callback() {
                                    override fun onCreate(db: SupportSQLiteDatabase) {
                                        super.onCreate(db)
                                    }

                                    override fun onOpen(db: SupportSQLiteDatabase) {
                                        super.onOpen(db)
                                    }
                                })
                                .build()
                    }
                }
            }
            return instance!!
        }
    }


}
