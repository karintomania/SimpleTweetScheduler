package com.example.twittertest.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "user_token")
class UserToken (
    @PrimaryKey()
    @ColumnInfo(name = "user_id")
        val userId: String,
    @ColumnInfo(name = "name")
    val name: String?,
    @ColumnInfo(name = "token")
        val token: String?,
    @ColumnInfo(name = "token_secret")
        val tokenSecret: String?
)
