package com.example.srbus.data.local.favorite

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.content.Context
import com.example.srbus.R
import com.example.srbus.data.remote.arrBus.ArrBusItem
import java.io.Serializable

@Entity(tableName = "FavoriteStationInBus")
data class FavoriteStationInBus (
    @PrimaryKey(autoGenerate = true) var id: Int?,
    @ColumnInfo(name = "arsId") var arsId: String,
    @ColumnInfo(name = "busRouteId") var busRouteId: String,
    @ColumnInfo(name = "rtNm") var rtNm: String,
    @ColumnInfo(name = "routeType") var routeType: String
): Serializable {

    fun getRouteTypeString(): String =
        when (routeType) {
            "0" -> ArrBusItem.RouteType.공용.name
            "1" -> ArrBusItem.RouteType.공항.name
            "2" -> ArrBusItem.RouteType.마을.name
            "3" -> ArrBusItem.RouteType.간선.name
            "4" -> ArrBusItem.RouteType.지선.name
            "5" -> ArrBusItem.RouteType.순환.name
            "6" -> ArrBusItem.RouteType.광역.name
            "7" -> ArrBusItem.RouteType.인천.name
            "8" -> ArrBusItem.RouteType.경기.name
            "9" -> ArrBusItem.RouteType.폐지.name
            else -> ArrBusItem.RouteType.폐지.name
        }

    fun getRouteTypeTextColor(context: Context): Int =
        when (routeType) {
            "0" -> context.resources.getColor(R.color.black)
            "1" -> context.resources.getColor(R.color.teal500)
            "2" -> context.resources.getColor(R.color.green900)
            "3" -> context.resources.getColor(R.color.blue500)
            "4" -> context.resources.getColor(R.color.green500)
            "5" -> context.resources.getColor(R.color.black)
            "6" -> context.resources.getColor(R.color.black)
            "7" -> context.resources.getColor(R.color.red500)
            "8" -> context.resources.getColor(R.color.teal500)
            "9" -> context.resources.getColor(R.color.black)
            else -> context.resources.getColor(R.color.black)
        }

    override fun toString(): String {
        return "FavoriteStationInBus(id=$id, arsId='$arsId', busRouteId='$busRouteId', rtNm='$rtNm', routeType='$routeType')"
    }
}