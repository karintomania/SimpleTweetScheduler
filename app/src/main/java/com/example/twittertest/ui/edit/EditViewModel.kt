package com.example.twittertest.ui.edit

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twittertest.database.TweetSchedule
import com.example.twittertest.database.TweetScheduleDao
import com.example.twittertest.database.TweetScheduleStatus
import kotlinx.coroutines.launch
import java.util.*

class EditViewModel(
    private val database: TweetScheduleDao,
    application: Application
) : AndroidViewModel(application) {
    // TODO: Implement the ViewModel

    private var tweetScheduleId: String

    init{
       tweetScheduleId = ""
    }

    private val tag ="EditViewModel"
    fun onSend(tweetContent:String){
        val tweetSchedule = TweetSchedule(status = TweetScheduleStatus.draft,tweetContent =  tweetContent, schedule = null, lastUpdate = Date())

        viewModelScope.launch {
            insertAll(tweetSchedule)
            Log.i(tag, tweetContent)
        }
    }

    private suspend fun insertAll(tweetSchedule: TweetSchedule){
        database.insertAll(tweetSchedule)
    }

}