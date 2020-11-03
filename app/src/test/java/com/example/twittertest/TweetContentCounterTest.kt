package com.example.twittertest

import com.example.twittertest.ui.TweetContentCounter
import org.junit.Assert.assertEquals
import org.junit.Test

class TweetContentCounterTest {
    @Test
    fun countEnglishTest(){
        val target = "aaa"
        val count = TweetContentCounter.count(target)

        assertEquals(3,count)
    }

    @Test
    fun countEnglishAndJapaneseTest(){
        val target = "aaaこんにちは"
        val count = TweetContentCounter.count(target)

        assertEquals(13,count)
    }

    @Test
    fun countUrlTest(){
        val target = "https://www.google.com/"
        val count = TweetContentCounter.count(target)

        assertEquals(22,count)
    }

    @Test
    fun countMultipleLineTest(){
        val target = "line1\nline2"
        val count = TweetContentCounter.count(target)

        assertEquals(11,count)
    }

    @Test
    fun replaceUrlTest(){
        val target = "https://www.google.com/"
        val result = TweetContentCounter.replaceUrl(target)

        assertEquals("abcdefghijklmnopqrstuv", result)

        val target2 = "https://kotlinlang.org/docs/reference/object-declarations.html#companion-objects"
        val result2 = TweetContentCounter.replaceUrl(target2)

        assertEquals("abcdefghijklmnopqrstuv", result2)
    }
}