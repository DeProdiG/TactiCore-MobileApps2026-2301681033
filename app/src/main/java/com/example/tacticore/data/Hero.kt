package com.example.tacticore.data

import androidx.annotation.DrawableRes

data class Hero(
    val id: Int,
    val name: String,
    val role: String,
    val description: String,
    @DrawableRes val imageResId: Int
)
