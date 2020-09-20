package com.example.twittertest

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.twittertest.database.AppDatabase
import com.example.twittertest.database.TweetSchedule
import com.example.twittertest.database.TweetScheduleDao
import com.example.twittertest.database.TweetScheduleStatus
import kotlinx.coroutines.runBlocking
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.io.IOException
import java.util.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    private lateinit var tweetScheduleDao: TweetScheduleDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        tweetScheduleDao = db.tweetScheduleDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetNight() {
        val tweetContent = "aaaaa"
        val status = TweetScheduleStatus.draft
        val schedule = Date()
        val lastUpate = Date()

        val tweetSchedule = TweetSchedule(status = status,  tweetContent = tweetContent,schedule = schedule, lastUpdate = lastUpate)

        runBlocking {

            tweetScheduleDao.insertAll(tweetSchedule)

            val tweetSchedules = tweetScheduleDao.getAll()
            assertEquals(tweetSchedules.value?.get(0)?.tweetContent, tweetContent)
        }
    }
}