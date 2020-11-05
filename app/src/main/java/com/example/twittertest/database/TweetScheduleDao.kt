package com.example.twittertest.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TweetScheduleDao {
    @Query("SELECT * FROM tweet_schedule")
    fun getAll(): LiveData<List<TweetSchedule>>

    @Query("SELECT * FROM tweet_schedule WHERE status = :status ORDER BY last_update DESC")
    fun selectTweetScheduleByStatus(status:String): LiveData<List<TweetSchedule>>

    @Query("SELECT * FROM tweet_schedule WHERE status IN ('scheduled', 'fail', 'sent') ORDER BY schedule ASC")
    fun selectTweetScheduleInSchedule(): LiveData<List<TweetSchedule>>

    @Query("SELECT * FROM tweet_schedule WHERE id = :id")
    fun selectTweetScheduleById(id:Long): TweetSchedule

    @Query("SELECT * FROM tweet_schedule WHERE id = :id")
    suspend fun selectTweetScheduleByIdSuspended(id:Long): TweetSchedule

    @Insert
    suspend fun insertAll(vararg ts: TweetSchedule)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tweetSchedule: TweetSchedule): Long

    @Delete
    fun delete(ts: TweetSchedule)

    @Query("DELETE FROM tweet_schedule WHERE id = :id")
    suspend fun deleteTweetScheduleById(id:Long)


}
