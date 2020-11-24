package com.example.twittertest.ui.edit

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.twittertest.database.TweetScheduleDao
import com.example.twittertest.database.UserTokenDao

class EditViewModelFactory(
    private val dataSource: TweetScheduleDao,
    private val userTokenDao: UserTokenDao,
    private val tweetId: Long,
    private val application: Application
) : ViewModelProvider.Factory {
   @Suppress("unchecked_cast")
   override fun <T : ViewModel?> create(modelClass: Class<T>): T {
       if (modelClass.isAssignableFrom(EditViewModel::class.java)) {
           return EditViewModel(dataSource,userTokenDao, tweetId, application) as T
       }
       throw IllegalArgumentException("Unknown ViewModel class")
   }
}