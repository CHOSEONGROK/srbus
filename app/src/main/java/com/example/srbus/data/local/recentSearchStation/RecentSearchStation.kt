package com.example.srbus.data.local.recentSearchStation

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.example.srbus.data.remote.searchStation.SearchStationItem

@Entity(tableName = "RecentSearchStation")
data class RecentSearchStation (
    @PrimaryKey(autoGenerate = true) var id: Int?,
    @ColumnInfo(name = "arsId") var arsId: String,
    @ColumnInfo(name = "posX") var posX: String,
    @ColumnInfo(name = "posY") var posY: String,
    @ColumnInfo(name = "stId") var stId: String,
    @ColumnInfo(name = "stNm") var stNm: String,
    @ColumnInfo(name = "tmX") var tmX: String,
    @ColumnInfo(name = "tmY") var tmY: String
) {
    fun convert(): SearchStationItem = SearchStationItem().also {
        it.id = this.id ?: -1
        it.arsId = this.arsId
        it.posX = this.posX
        it.posY = this.posY
        it.stId = this.stId
        it.stNm = this.stNm
        it.tmX = this.stNm
        it.tmY = this.tmY
    }

    override fun toString(): String {
        return "RecentSearchStation(id=$id, arsId='$arsId', posX='$posX', posY='$posY', stId='$stId', stNm='$stNm', tmX='$tmX', tmY='$tmY')"
    }
}