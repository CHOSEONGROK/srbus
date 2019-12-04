package com.example.srbus.data.local.searchStationHistory

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = [SearchStationHistory::class], version = 1, exportSchema = false)
abstract class SearchStationHistoryDB : RoomDatabase() {
    abstract fun searchStationHistoryDao(): SearchStationHistoryDao

    companion object {
        private var INSTANCE: SearchStationHistoryDB? = null
        private val lock = Any()

        fun getInstance(context: Context): SearchStationHistoryDB {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        SearchStationHistoryDB::class.java, "SearchStationHistoryDB.db")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                }
                return INSTANCE!!
            }
        }
    }
}