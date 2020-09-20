package com.example.twittertest.ui.draft

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twittertest.database.TweetSchedule
import com.example.twittertest.database.TweetScheduleDao
import kotlinx.coroutines.launch

class DraftViewModel (
    private val database: TweetScheduleDao,
    application: Application
) : AndroidViewModel(application) {

   val drafts = database.getAll()

}