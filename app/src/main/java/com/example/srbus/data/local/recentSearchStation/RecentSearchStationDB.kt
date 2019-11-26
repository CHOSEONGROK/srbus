package com.example.srbus.data.local.recentSearchStation

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = [RecentSearchStation::class], version = 3, exportSchema = false)
abstract class RecentSearchStationDB : RoomDatabase() {
    abstract fun recentSearchStationDao(): RecentSearchStationDao

    companion object {
        private var INSTANCE: RecentSearchStationDB? = null
        private val lock = Any()

        fun getInstance(context: Context): RecentSearchStationDB {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        RecentSearchStationDB::class.java, "RecentSearchStationDB.db")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                }
                return INSTANCE!!
            }
        }
    }
}