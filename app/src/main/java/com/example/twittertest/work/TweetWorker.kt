package com.example.twittertest.work

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.twittertest.database.AppDatabase
import com.example.twittertest.database.TweetSchedule
import twitter4j.TwitterFactory

class TweetWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    val tag = "TweetWorker"
    val context = ctx
    val datasource = AppDatabase.getInstance(context).tweetScheduleDao

    override fun doWork(): Result {

        val id = inputData.getLong("id",0L)

        val tweetSchedule = datasource.selectTweetScheduleById(id)

        val twitter = TwitterFactory().getInstance()
//        twitter.updateStatus(tweetSchedule.tweetContent)

        Log.i(tag,tweetSchedule.tweetContent)

        datasource.delete(tweetSchedule)

        return Result.success()
    }
}
