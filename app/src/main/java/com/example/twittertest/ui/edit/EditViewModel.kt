package com.example.twittertest.ui.edit

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twittertest.database.TweetSchedule
import com.example.twittertest.database.TweetScheduleDao
import com.example.twittertest.database.TweetScheduleStatus
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.*

class EditViewModel(
    private val database: TweetScheduleDao,
    application: Application
) : AndroidViewModel(application) {

    private val tag ="EditViewModel"
    private var tweetScheduleId: String

    init{
       tweetScheduleId = ""
    }


    fun onSave(tweetContent:String){
        val tweetSchedule = TweetSchedule(status = TweetScheduleStatus.draft,tweetContent =  tweetContent, schedule = null, lastUpdate = LocalDateTime.now())

        viewModelScope.launch {
            insertAll(tweetSchedule)
            Log.i(tag, tweetContent)
        }
    }

    fun onSchedule(tweetContent:String, scheduleDateTime:LocalDateTime){
        val tweetSchedule = TweetSchedule(status = TweetScheduleStatus.draft,tweetContent =  tweetContent, schedule = scheduleDateTime, lastUpdate = LocalDateTime.now())

        viewModelScope.launch {
            insertAll(tweetSchedule)
            Log.i(tag, tweetContent)
        }
    }
    private suspend fun insertAll(tweetSchedule: TweetSchedule){
        database.insertAll(tweetSchedule)
    }

}