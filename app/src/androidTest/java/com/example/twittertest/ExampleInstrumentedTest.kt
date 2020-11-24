package com.example.twittertest

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.twittertest.database.*
import kotlinx.coroutines.runBlocking
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.io.IOException
import java.time.LocalDateTime
import java.util.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    private lateinit var tweetScheduleDao: TweetScheduleDao
    private lateinit var userTokenDao: UserTokenDao
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
        userTokenDao = db.userTokenDao
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
        val schedule = LocalDateTime.now()
        val lastUpate = LocalDateTime.now()

        val tweetSchedule = TweetSchedule(status = status,  tweetContent = tweetContent,schedule = schedule, lastUpdate = lastUpate)

        runBlocking {

            tweetScheduleDao.insertAll(tweetSchedule)

            val tweetSchedules = tweetScheduleDao.getAll()
            assertEquals(tweetSchedules.value?.get(0)?.tweetContent, tweetContent)
        }
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetUserToken() {
        val userToken = UserToken("karinto", "かりんとう", "tokenxx", "tokensecretxxx")

        runBlocking {

            userTokenDao.insert(userToken)

            val userTokenSelected = userTokenDao.selectUserTokenById("karinto")
            assertEquals(userTokenSelected.name, "かりんとう")
        }
    }

}