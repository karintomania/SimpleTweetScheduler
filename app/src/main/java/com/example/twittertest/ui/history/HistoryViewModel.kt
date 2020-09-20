package com.example.twittertest.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.twittertest.database.TweetScheduleDao

class HistoryViewModel(
    private val database: TweetScheduleDao,
    application: Application
) : AndroidViewModel(application) {
    val histories = database.getAll()
}