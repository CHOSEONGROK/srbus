package com.example.srbus.data.local.favorite

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = [FavoriteStationInBus::class], version = 7, exportSchema = false)
abstract class FavoriteStationInBusDB : RoomDatabase() {
    abstract fun favoriteStationInBusDao(): FavoriteStationInBusDao

    companion object {
        private var INSTANCE: FavoriteStationInBusDB? = null
        private val lock = Any()

        fun getInstance(context: Context): FavoriteStationInBusDB {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        FavoriteStationInBusDB::class.java, "FavoriteStationInBusDB.db")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE!!
        }
    }
}