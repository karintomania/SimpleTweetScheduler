package com.example.twittertest.ui.schedule

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.twittertest.database.TweetScheduleDao
import kotlinx.coroutines.launch

class ScheduleViewModel(
    private val database: TweetScheduleDao,
    application: Application
) : AndroidViewModel(application) {
    val scheduledTweets = database.selectTweetScheduleInSchedule()

    fun onScheduleDeleteClicked(id: Long) {

        viewModelScope.launch {
            deleteTweet(id)
        }
    }

    suspend fun deleteTweet(id: Long){
        database.deleteTweetScheduleById(id)
    }

}