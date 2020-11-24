package com.example.twittertest.ui.edit

import android.app.Application
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.twittertest.database.*
import com.example.twittertest.ui.TweetContentCounter
import com.example.twittertest.work.TweetWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

class EditViewModel(
    private val database: TweetScheduleDao,
    private val userTokenDao: UserTokenDao,
    private val tweetId: Long,
    application: Application
) : AndroidViewModel(application) {

    private val tag ="EditViewModel"
    private val workManager = WorkManager.getInstance()
    // LiveData
    val tweetContent = MutableLiveData<String>()
    val tweetContentLength = Transformations.map(tweetContent){
        TweetContentCounter.countRestCharacter(it).toString()
//        it.length.toString()
    }
    val scheduleDateTime = MutableLiveData<LocalDateTime>()
    val scheduleDateString = Transformations.map(scheduleDateTime){
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        it.format(formatter)
    }

    val scheduleTimeString = Transformations.map(scheduleDateTime){
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        it.format(formatter)
    }

    init{
        scheduleDateTime.value = LocalDateTime.now()
        tweetContent.value = ""
        val tweetSchedule = getTweetSchedule(tweetId)

    }


    fun onSave(tweetContent:String){
        val tweetSchedule:TweetSchedule;
        if(tweetId == -1L){
            tweetSchedule = TweetSchedule(status = TweetScheduleStatus.draft,tweetContent =  tweetContent, schedule = null, lastUpdate = LocalDateTime.now())
        }else{
            tweetSchedule = TweetSchedule(id = tweetId, status = TweetScheduleStatus.draft,tweetContent =  tweetContent, schedule = null, lastUpdate = LocalDateTime.now())
        }

        viewModelScope.launch {
            insert(tweetSchedule)
            Log.i(tag, tweetContent)

        }
    }

    fun onSchedule(tweetContent:String, scheduleDateTime:LocalDateTime){
        val tweetSchedule:TweetSchedule;
        if(tweetId == -1L){
            tweetSchedule = TweetSchedule(status = TweetScheduleStatus.scheduled,tweetContent =  tweetContent, schedule = scheduleDateTime, lastUpdate = LocalDateTime.now())
        }else{
            tweetSchedule = TweetSchedule(id = tweetId, status = TweetScheduleStatus.scheduled,tweetContent =  tweetContent, schedule = scheduleDateTime, lastUpdate = LocalDateTime.now())
        }

        val initialDelayMinute = calculateInitialDelayMinute(scheduleDateTime)

        viewModelScope.launch {
            val insertedId = insert(tweetSchedule)
            Log.i(tag, tweetContent)
            setTweetWork(insertedId, initialDelayMinute)
        }

    }
    private suspend fun insert(tweetSchedule: TweetSchedule): Long {
        return database.insert(tweetSchedule)
    }

    private fun setTweetWork(tweetId:Long, initialDelayMinute:Long) {
        val builder = Data.Builder()
        builder.putLong("id", tweetId)

        val tweetRequest = OneTimeWorkRequestBuilder<TweetWorker>()
            .setInitialDelay(initialDelayMinute, TimeUnit.MINUTES)
            .setInputData(builder.build())
            .build()

        workManager.enqueue(tweetRequest)
    }

    private fun calculateInitialDelayMinute(scheduleDateTime: LocalDateTime) : Long{
        val minutes: Long = ChronoUnit.MINUTES.between(LocalDateTime.now(), scheduleDateTime)
        Log.i(tag, "diff minutes = ${minutes}")
        return minutes
    }

    private fun getTweetSchedule(tweetId:Long): TweetSchedule?{
        var tweetSchedule: TweetSchedule? = null
        viewModelScope.launch {
            tweetSchedule = selectTweetScheduleById(tweetId)
            tweetSchedule?.let{
                tweetContent.value = it.tweetContent
//                scheduleDateTime.value = it.schedule
                it.schedule?.let{
                    scheduleDateTime.value = it
                }
            }
        }

        return tweetSchedule
    }

    private suspend fun selectTweetScheduleById(tweetId: Long): TweetSchedule{
           return database.selectTweetScheduleByIdSuspended(tweetId)
    }

    suspend fun storeUserToken(userToken: UserToken){
        userTokenDao.insert(userToken)
    }
}