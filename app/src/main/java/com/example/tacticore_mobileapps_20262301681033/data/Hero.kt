package com.example.tacticore_mobileapps_20262301681033.data

import androidx.annotation.DrawableRes

data class Hero(
    val id: Int,
    val name: String,
    val role: String,
    val description: String,
    @DrawableRes val imageResId: Int
)
