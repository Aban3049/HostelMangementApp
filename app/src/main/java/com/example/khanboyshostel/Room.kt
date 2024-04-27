package com.example.khanboyshostel

data class Room(
    var roomId: String = "",
    var roomNumber: String = "",
    var roomBeds: Int = 0,
    var roomCapacity: String = "",
    var occupied: Boolean = false,
    var beds: List<Bed> = listOf()
) {
    companion object {
        fun fromMap(map: Map<String, Any>): Room {
            return Room(
                roomId = map["roomId"].toString(),
                roomNumber = map["roomNumber"].toString(),
                roomBeds = (map["roomBeds"] as Long).toInt(),
                roomCapacity = map["roomCapacity"].toString(),
                occupied = map["occupied"] as Boolean,

                )
        }
    }

}

data class Bed(
    var bedId: String = "",
    var bedNumber: String = "",
    var occupied: Boolean = false
)

data class Complains(
    var complain: String = "",
    var name: String = "",
    var timestamp: Long = 0
)

data class Students(

    var studentName: String = "",
    var fatherName: String = "",
    var studentCellNo: String = "",
    var fatherCellNo: String = "",
    var Address: String = "",
    var roomNumber: String = "",
    var id: String = "",
    var timestamp:Long = 0

)
