package com.ilsamil.readingdiarycleanarchitecture.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ReadingDay(
    var year: String,
    var month: String,
    var day: String,
    var book: String,
    var readSt: String?,
    var readEd: Int?,
    var maxPage : String?
) {
    @PrimaryKey(autoGenerate = true) var id : Int = 0
}