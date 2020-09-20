package com.example.twittertest.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*




@Entity(tableName = "tweet_schedule")
class TweetSchedule (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "user_id")
    val userId: String? = "test",
    @ColumnInfo(name = "status")
    val status: String?,
    @ColumnInfo(name = "tweet_content")
    val tweetContent: String?,
    @ColumnInfo(name = "schedule")
    val schedule: Date?,
    @ColumnInfo(name = "last_update")
    val lastUpdate: Date?
)
