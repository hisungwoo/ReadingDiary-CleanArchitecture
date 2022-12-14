package com.ilsamil.readingdiarycleanarchitecture.data.db.dao

import androidx.room.*
import com.ilsamil.readingdiarycleanarchitecture.data.db.entity.ReadingDay

@Dao
interface ReadingDao {
    @Query("SELECT * FROM ReadingDay WHERE year = :year AND month = :month AND day = :day")
    suspend fun selectReadingDay(year : String, month : String, day : String) : ReadingDay

    @Query("SELECT DISTINCT day FROM ReadingDay WHERE year = :year AND month = :month ORDER BY day")
    suspend fun selectReadingDate(year : String, month : String) : List<String>

    @Query("SELECT * FROM ReadingDay WHERE readEd = (SELECT MAX(readEd) FROM ReadingDay WHERE book = :book) AND book = :book")
    suspend fun selectMaxRead(book : String) : ReadingDay

    @Query("SELECT * FROM ReadingDay WHERE readEd = (SELECT Min(readEd) FROM ReadingDay WHERE book = :book) AND book = :book")
    suspend fun selectMinRead(book : String) : ReadingDay

    @Query("SELECT COUNT(*) FROM ReadingDay WHERE book = :book")
    suspend fun selectReadCnt(book : String) : String

    @Query("DELETE FROM ReadingDay WHERE year = :year AND month = :month AND day = :day")
    suspend fun deleteReadingDay(year : String, month : String, day : String) : Int

    @Query("DELETE FROM ReadingDay WHERE book = :book")
    suspend fun deleteAllReadingDay(book : String) : Int

    @Insert
    suspend fun insertReadingDay(data : ReadingDay) : Long

    @Query("UPDATE ReadingDay SET book = :book, readSt = :readSt, readEd = :readEd, maxPage = :maxPage WHERE year = :year and month = :month and day = :day")
    suspend fun updateReadingDay(year : String, month : String, day : String, book: String, readSt : String, readEd : String, maxPage : String)

    @Query("SELECT DISTINCT * FROM ReadingDay WHERE readEd = maxPage")
    suspend fun selectFinishRead() : List<ReadingDay>

    @Query("SELECT count(*) FROM ReadingDay")
    suspend fun selectReadingCount() : Int
}