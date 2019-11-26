package com.example.srbus.data.local.favorite

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface FavoriteStationDao {

    @Query("SELECT * FROM FavoriteStation")
    fun getAllFavoriteStation(): List<FavoriteStation>

    @Query("SELECT * FROM FavoriteStation WHERE arsId = :arsId")
    fun getFavoriteStation(arsId: String): FavoriteStation?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg favoriteStation: FavoriteStation)

    @Query("DELETE FROM FavoriteStation WHERE arsId = :arsId")
    fun delete(arsId: String)

    @Query("DELETE FROM FavoriteStation")
    fun deleteAll()
}