package com.example.twittertest.ui.edit

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.twittertest.database.TweetSchedule
import com.example.twittertest.database.TweetScheduleDao
import com.example.twittertest.database.TweetScheduleStatus
import com.example.twittertest.work.TweetWorker
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.*

class EditViewModel(
    private val database: TweetScheduleDao,
    application: Application
) : AndroidViewModel(application) {

    private val tag ="EditViewModel"
    private var tweetScheduleId: String
    private val workManager = WorkManager.getInstance()
    init{
       tweetScheduleId = ""
    }


    fun onSave(tweetContent:String){
        val tweetSchedule = TweetSchedule(status = TweetScheduleStatus.draft,tweetContent =  tweetContent, schedule = null, lastUpdate = LocalDateTime.now())

        viewModelScope.launch {
            insert(tweetSchedule)
            Log.i(tag, tweetContent)

        }
    }

    fun onSchedule(tweetContent:String, scheduleDateTime:LocalDateTime){
        val tweetSchedule = TweetSchedule(status = TweetScheduleStatus.scheduled,tweetContent =  tweetContent, schedule = scheduleDateTime, lastUpdate = LocalDateTime.now())

        viewModelScope.launch {
            val insertedId = insert(tweetSchedule)
            Log.i(tag, tweetContent)
            setTweetWork(insertedId)
        }


    }
    private suspend fun insert(tweetSchedule: TweetSchedule): Long {
        return database.insert(tweetSchedule)
    }

    private fun setTweetWork(tweetId:Long) {
        val builder = Data.Builder()
        builder.putLong("id", tweetId)

        val tweetRequest = OneTimeWorkRequestBuilder<TweetWorker>()
            .setInputData(builder.build())
            .build()

        workManager.enqueue(tweetRequest)
    }

}