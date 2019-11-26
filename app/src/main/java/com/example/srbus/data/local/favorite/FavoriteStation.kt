package com.example.srbus.data.local.favorite

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "FavoriteStation")
data class FavoriteStation(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    @ColumnInfo(name = "arsId") var arsId: String,
    @ColumnInfo(name = "stId") var stId: String,
    @ColumnInfo(name = "stNm") var stNm: String,
    @ColumnInfo(name = "nextStation") var nextStation: String?
): Serializable {

    override fun toString(): String {
        return "FavoriteStation(id=$id, arsId='$arsId', stId='$stId', stNm='$stNm')"
    }
}