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


    @Insert
    suspend fun insertAll(vararg ts: TweetSchedule)

    @Delete
    suspend fun delete(ts: TweetSchedule)

}
