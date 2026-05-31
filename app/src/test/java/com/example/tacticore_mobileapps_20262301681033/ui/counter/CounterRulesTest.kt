package com.example.tacticore_mobileapps_20262301681033.utils

import com.example.tacticore_mobileapps_20262301681033.ui.counter.CounterRules
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
