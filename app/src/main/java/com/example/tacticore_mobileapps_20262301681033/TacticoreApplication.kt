package com.example.tacticore_mobileapps_20262301681033

import android.app.Application
import com.example.tacticore_mobileapps_20262301681033.data.HeroRepository

class TacticoreApplication : Application() {
    lateinit var repository: HeroRepository
        private set

    override fun onCreate() {
        super.onCreate()
        repository = HeroRepository(applicationContext)
    }
}
