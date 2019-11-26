package com.example.srbus.data.local.favorite

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = [FavoriteStation::class], version = 7, exportSchema = false)
abstract class FavoriteStationDB : RoomDatabase() {
    abstract fun favoriteStationDao(): FavoriteStationDao

    companion object {
        private var INSTANCE: FavoriteStationDB? = null
        private val lock = Any()

        fun getInstance(context: Context): FavoriteStationDB {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        FavoriteStationDB::class.java, "FavoriteStationDB.db")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                }
                return INSTANCE!!
            }
        }
    }
}