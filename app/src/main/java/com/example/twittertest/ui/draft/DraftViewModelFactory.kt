package com.example.twittertest.ui.draft

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.twittertest.database.TweetScheduleDao
import com.example.twittertest.ui.draft.DraftViewModel

class DraftViewModelFactory(
    private val dataSource: TweetScheduleDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DraftViewModel::class.java)) {
            return DraftViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
