package com.example.srbus.data.local.favorite

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface FavoriteStationInBusDao {

    @Query("SELECT * FROM FavoriteStationInBus")
    fun getAll(): List<FavoriteStationInBus>

    @Query("SELECT * FROM FavoriteStationInBus WHERE arsId = :arsId")
    fun getItemsByArsId(arsId: String): List<FavoriteStationInBus>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favoriteStationInBus: FavoriteStationInBus)

    @Query("DELETE FROM FavoriteStationInBus WHERE busRouteId = :busRouteId")
    fun deleteByBusRouteId(busRouteId: String)

    @Query("DELETE FROM FavoriteStationInBus WHERE arsId = :arsId")
    fun deleteByArsId(arsId: String)

    @Query("DELETE FROM FavoriteStationInBus")
    fun deleteAll()
}