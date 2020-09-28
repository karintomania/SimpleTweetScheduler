package com.example.twittertest.work

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.twittertest.database.AppDatabase

class TweetWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    val tag = "TweetWorker"
    val context = ctx
    override fun doWork(): Result {

        val datasource = AppDatabase.getInstance(context).tweetScheduleDao
        val id = inputData.getLong("id",0L)
        val tweetSchedule = datasource.selectTweetScheduleById(id)

        Log.i(tag,tweetSchedule.tweetContent)

        datasource.delete(tweetSchedule)

        return Result.success()
    }
}
