package com.example.tacticore

import android.app.Application
import com.example.tacticore.data.AuthRepository
import com.example.tacticore.data.HeroRepository

class TacticoreApplication : Application() {
    lateinit var repository: HeroRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val userId = AuthRepository(this).getCurrentUserId()
        repository = HeroRepository(applicationContext, userId)
    }

    fun refreshRepository() {
        val userId = AuthRepository(this).getCurrentUserId()
        repository = HeroRepository(applicationContext, userId)
    }
}
