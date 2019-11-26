package com.example.srbus.data

data class Bus(
    var number: Int,
    var nextStation: String,
    var firstArrBusRemainingTime: String,
    var firstArrBusRemainingStation: String,
    var secondArrBusRemainingTime: String,
    var secondArrBusRemainingStation: String,
    var isFavorite: Boolean,
    var isAlarm: Boolean
) {
}