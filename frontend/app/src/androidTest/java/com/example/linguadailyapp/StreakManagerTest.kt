package com.example.linguadailyapp

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.linguadailyapp.utils.preferences.StreakCounter
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class StreakCounterTest {

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        StreakCounter.resetStreak(context)
    }

    @After
    fun tearDown() {
        StreakCounter.resetStreak(context)
    }

    @Test
    fun firstTimeOpenApp_streakIsOne() {
        val streak = StreakCounter.updateStreak(context)
        assertEquals(1, streak)
    }

    @Test
    fun openAppTwiceInSameDay_streakRemainsOne() {
        StreakCounter.updateStreak(context)
        val streak = StreakCounter.updateStreak(context)
        assertEquals(1, streak)
    }

    @Test
    fun openAppOnConsecutiveDays_streakIncrements() {
        // Open app on day 1
        val today = LocalDate.now()
        StreakCounter.updateStreakWithDate(context, today)

        // Open app on day 2
        val tomorrow = today.plusDays(1)
        val streak = StreakCounter.updateStreakWithDate(context, tomorrow)

        assertEquals(2, streak)
    }

    @Test
    fun missADay_streakResetsToOne() {
        // Open app on day 1
        val today = LocalDate.now()
        StreakCounter.updateStreakWithDate(context, today)

        // Skip a day, open app on day 3
        val dayAfterTomorrow = today.plusDays(2)
        val streak = StreakCounter.updateStreakWithDate(context, dayAfterTomorrow)

        assertEquals(1, streak)
    }

    @Test
    fun buildStrongerStreak_thenMissADay_resetsToOne() {
        val today = LocalDate.now()

        // Day 1
        StreakCounter.updateStreakWithDate(context, today)

        // Day 2
        StreakCounter.updateStreakWithDate(context, today.plusDays(1))

        // Day 3
        val streak = StreakCounter.updateStreakWithDate(context, today.plusDays(2))
        assertEquals(3, streak)

        // Skip a day, open on Day 5
        val resetStreak = StreakCounter.updateStreakWithDate(context, today.plusDays(4))
        assertEquals(1, resetStreak)
    }

    @Test
    fun getCurrentStreak_returnsCorrectValueWithoutUpdating() {
        // First set up a streak of 3
        val today = LocalDate.now()
        StreakCounter.updateStreakWithDate(context, today)
        StreakCounter.updateStreakWithDate(context, today.plusDays(1))
        StreakCounter.updateStreakWithDate(context, today.plusDays(2))

        // Now just get the current streak without updating
        val currentStreak = StreakCounter.getCurrentStreak(context)
        assertEquals(3, currentStreak)
    }

    @Test
    fun resetStreak_setsStreakToZero() {
        // First build up a streak
        StreakCounter.updateStreak(context)
        assertEquals(1, StreakCounter.getCurrentStreak(context))

        // Now reset it
        StreakCounter.resetStreak(context)
        assertEquals(0, StreakCounter.getCurrentStreak(context))
    }
}