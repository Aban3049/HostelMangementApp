package com.example.khanboyshostel

class modelStudetns {

    constructor()
    constructor(
        studentName: String,
        fatherName: String,
        studentCellNo: String,
        fatherCellNo: String,
        Address: String,
        roomNumber: String,
        id: String,
        timestamp: Long
    ) {
        this.studentName = studentName
        this.fatherName = fatherName
        this.studentCellNo = studentCellNo
        this.fatherCellNo = fatherCellNo
        this.Address = Address
        this.roomNumber = roomNumber
        this.id = id
        this.timestamp = timestamp
    }

    var studentName: String = ""
    var fatherName: String = ""
    var studentCellNo: String = ""
    var fatherCellNo: String = ""
    var Address: String = ""
    var roomNumber: String = ""
    var id: String = ""
    var timestamp:Long = 0



}