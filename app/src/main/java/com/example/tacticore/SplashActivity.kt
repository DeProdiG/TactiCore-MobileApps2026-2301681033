package com.example.tacticore   // Уверете се, че пакетът съвпада с вашия

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val logo = findViewById<ImageView>(R.id.logo)
        val appName = findViewById<TextView>(R.id.appName)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        fadeIn.duration = 1500
        logo.startAnimation(fadeIn)
        logo.alpha = 1f

        appName.startAnimation(fadeIn)
        appName.alpha = 1f

        Handler(Looper.getMainLooper()).postDelayed({
            progressBar.visibility = android.view.View.VISIBLE
        }, 1000)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 3000)
    }
}