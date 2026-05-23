package com.example.tacticore

import android.app.Application
import com.example.tacticore.data.HeroRepository

class TacticoreApplication : Application() {
    lateinit var repository: HeroRepository
        private set

    override fun onCreate() {
        super.onCreate()
        repository = HeroRepository(applicationContext)
    }
}