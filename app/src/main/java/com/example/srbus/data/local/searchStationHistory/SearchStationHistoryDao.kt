package com.example.srbus.data.local.searchStationHistory

import android.arch.persistence.room.*

@Dao
interface SearchStationHistoryDao {

    @Query("SELECT * FROM SearchStationHistory")
    fun selectAll(): List<SearchStationHistory>

    @Query("SELECT * FROM SearchStationHistory WHERE arsId = :arsId")
    fun select(arsId: String): List<SearchStationHistory>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(searchStationHistory: SearchStationHistory)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(searchStationHistories: List<SearchStationHistory>)

//    @Delete
//    fun delete(vararg stations: SearchStationHistory)

    @Query("DELETE FROM SearchStationHistory WHERE arsId = :arsId")
    fun delete(arsId: String)

    @Query("DELETE FROM SearchStationHistory")
    fun deleteAll()
}