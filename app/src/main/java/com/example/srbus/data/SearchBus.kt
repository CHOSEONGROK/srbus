package com.example.srbus.data

import android.content.Context
import android.content.res.ColorStateList
import com.example.srbus.R

data class SearchBus(
    var number: Int,
    var city: String,
    var type: String,
    var isFavorite: Boolean
) {
    fun getFavoriteColorStateList(context: Context) = if (isFavorite)
        ColorStateList(arrayOf(intArrayOf()), intArrayOf(context.resources.getColor(R.color.yellow500)))
    else
        ColorStateList(arrayOf(intArrayOf()), intArrayOf(context.resources.getColor(R.color.gray300)))
}