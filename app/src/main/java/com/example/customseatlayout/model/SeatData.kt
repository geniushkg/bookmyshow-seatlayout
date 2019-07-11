package com.example.customseatlayout.model

data class SeatData (
    var type:String,
    var status:String,
    var areaCode:String
)

data class RowData(
    var seatList: ArrayList<SeatData>,
    var areaCode: String
)

data class SeatsResponse(
    var rowList: ArrayList<RowData>,
    var status: String
)