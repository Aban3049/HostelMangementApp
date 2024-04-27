package com.example.khanboyshostel

import java.io.Serializable

data class modelRooms(
    var roomId: String = "",
    var roomNumber: String = "",
    var capacity: Int = 0,
    var type: String = "",
    var isOccupied: Boolean = false,
    var beds: List<Bed> = emptyList()
): Serializable

