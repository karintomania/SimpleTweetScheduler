package com.example.twittertest.ui.history

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twittertest.database.TweetScheduleDao
import com.example.twittertest.database.TweetScheduleStatus
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val database: TweetScheduleDao,
    application: Application
) : AndroidViewModel(application) {
    val histories = database.selectTweetScheduleInHistory()

    fun onHistoryDeleteClicked(id: Long) {

        viewModelScope.launch {
            deleteTweet(id)
        }
    }

    suspend fun deleteTweet(id: Long){
        database.deleteTweetScheduleById(id)
    }

}