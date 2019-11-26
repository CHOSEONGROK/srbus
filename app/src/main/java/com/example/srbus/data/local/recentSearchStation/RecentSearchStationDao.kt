package com.example.srbus.data.local.recentSearchStation

import android.arch.persistence.room.*

@Dao
interface RecentSearchStationDao {

    @Query("SELECT * FROM RecentSearchStation")
    fun getAllRecentSearchStation(): List<RecentSearchStation>

    @Query("SELECT * FROM RecentSearchStation WHERE arsId = :arsId")
    fun getRecentSearchStationByArsId(arsId: String): List<RecentSearchStation>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(recentSearchStation: RecentSearchStation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(recentSearchStations: List<RecentSearchStation>)

//    @Delete
//    fun delete(vararg stations: RecentSearchStation)

    @Query("DELETE FROM RecentSearchStation WHERE arsId = :arsId")
    fun delete(arsId: String)

    @Query("DELETE FROM RecentSearchStation")
    fun deleteAll()
}