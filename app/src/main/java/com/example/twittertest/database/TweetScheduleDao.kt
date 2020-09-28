package com.example.twittertest.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TweetScheduleDao {
    @Query("SELECT * FROM tweet_schedule")
    fun getAll(): LiveData<List<TweetSchedule>>

    @Query("SELECT * FROM tweet_schedule WHERE status = :status")
    fun selectTweetScheduleByStatus(status:String): LiveData<List<TweetSchedule>>

    @Query("SELECT * FROM tweet_schedule WHERE status IN ('scheduled', 'fail', 'sent')")
    fun selectTweetScheduleInHistory(): LiveData<List<TweetSchedule>>

    @Query("SELECT * FROM tweet_schedule WHERE id = :id")
    fun selectTweetScheduleById(id:Long): TweetSchedule

    @Insert
    suspend fun insertAll(vararg ts: TweetSchedule)

    @Insert
    suspend fun insert(tweetSchedule: TweetSchedule): Long

    @Delete
    fun delete(ts: TweetSchedule)

}
