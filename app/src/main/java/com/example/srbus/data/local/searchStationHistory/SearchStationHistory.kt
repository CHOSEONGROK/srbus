package com.example.srbus.data.local.searchStationHistory

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.content.Context
import android.support.v4.content.ContextCompat
import com.example.srbus.R
import com.example.srbus.data.remote.searchStation.SearchStationItem

@Entity(tableName = "SearchStationHistory")
data class SearchStationHistory (
    @PrimaryKey(autoGenerate = true) var id: Int?,
    @ColumnInfo(name = "arsId") var arsId: String,
    @ColumnInfo(name = "posX") var posX: String,
    @ColumnInfo(name = "posY") var posY: String,
    @ColumnInfo(name = "stId") var stId: String,
    @ColumnInfo(name = "stNm") var stNm: String,
    @ColumnInfo(name = "tmX") var tmX: String,
    @ColumnInfo(name = "tmY") var tmY: String
) {
    var isFavorite = false

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

    fun getFavoriteColor(context: Context): Int = when (isFavorite) {
        true -> ContextCompat.getColor(context, R.color.yellow500)
        false -> ContextCompat.getColor(context, R.color.gray300)
    }

    override fun toString(): String {
        return "SearchStationHistory(id=$id, arsId='$arsId', posX='$posX', posY='$posY', stId='$stId', stNm='$stNm', tmX='$tmX', tmY='$tmY', isFavorite=$isFavorite)"
    }
}