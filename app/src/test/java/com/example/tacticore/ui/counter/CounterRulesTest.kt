package com.example.tacticore.utils

import com.example.tacticore.ui.counter.CounterRules
import org.junit.Assert.*
import org.junit.Test

class CounterRulesTest {
    @Test
    fun testGetCountersForEnemy() {
        val counters = CounterRules.getCountersForEnemy("Tracer")
        assertTrue(counters.contains("Winston"))
        assertTrue(counters.contains("Torbjorn"))
    }

    @Test
    fun testGetCountersForUnknown() {
        val counters = CounterRules.getCountersForEnemy("NonExistent")
        assertTrue(counters.isEmpty())
    }
}
