package com.example.twittertest.ui.draft

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twittertest.database.TweetSchedule
import com.example.twittertest.database.TweetScheduleDao
import com.example.twittertest.database.TweetScheduleStatus
import kotlinx.coroutines.launch

class DraftViewModel (
    private val database: TweetScheduleDao,
    application: Application
) : AndroidViewModel(application) {

   val drafts = database.selectTweetScheduleByStatus(TweetScheduleStatus.draft)

    fun onDraftDeleteClicked(id: Long) {
        Log.i("DraftViewModel", "ID=${id}")

        viewModelScope.launch {
            deleteTweet(id)
        }
    }

     suspend fun deleteTweet(id: Long){
        database.deleteTweetScheduleById(id)
    }
}