package com.example.twittertest.database

import androidx.lifecycle.LiveData
import androidx.room.*

    @Dao
    interface UserTokenDao {
        @Query("SELECT * FROM user_token")
        fun getAll(): LiveData<List<UserToken>>

        @Query("SELECT * FROM user_token")
        suspend fun getAllSuspended(): List<UserToken>

        @Query("SELECT * FROM user_token WHERE user_id = :id")
        fun selectUserTokenById(id:String): UserToken

        @Insert
        suspend fun insertAll(vararg ts: UserToken)

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insert(userToken: UserToken)

        @Delete
        fun delete(ts: UserToken)

        @Query("DELETE FROM user_token WHERE user_id = :id")
        suspend fun deleteUserTokenById(id:String)


    }
