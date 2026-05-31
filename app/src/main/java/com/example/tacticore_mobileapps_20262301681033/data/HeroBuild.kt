package com.example.tacticore_mobileapps_20262301681033.data

data class HeroBuild(
    val id: Long = 0,
    val heroId: Int,
    val mode: String,
    val userNotes: String,
    val rating: Int,
    val stadiumItems: String? = null,
    val stadiumGadgets: String? = null,
    val stadiumPower: String? = null,
    val quickPlayPerks: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
