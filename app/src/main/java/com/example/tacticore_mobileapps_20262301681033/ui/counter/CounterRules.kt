package com.example.tacticore.ui.counter

object CounterRules {
    private val counters = mapOf(
        "Tracer" to listOf("Winston", "Torbjorn"),
        "Reinhardt" to listOf("Bastion", "Pharah"),
        "Mercy" to listOf("Genji", "Tracer"),
        "Genji" to listOf("Winston", "Moira"),
        "Winston" to listOf("Reaper", "Bastion")
    )

    fun getCountersForEnemy(enemyName: String): List<String> {
        return counters[enemyName] ?: emptyList()
    }
}