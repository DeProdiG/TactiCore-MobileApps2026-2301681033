package com.example.tacticore.ui.qr

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tacticore.TacticoreApplication
import com.example.tacticore.data.HeroBuild
import com.example.tacticore.databinding.ActivityQrScannerBinding
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import kotlinx.coroutines.launch

class QRScannerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQrScannerBinding

    private val scanLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            processScannedData(result.contents)
        } else {
            Toast.makeText(this, "Сканирането е отказано", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStartScan.setOnClickListener {
            val options = ScanOptions()
            options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
            options.setPrompt("Сканирайте QR код")
            options.setCameraId(0)
            options.setBeepEnabled(true)
            options.setOrientationLocked(false)
            scanLauncher.launch(options)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun processScannedData(data: String) {
        val parts = data.split("|")
        if (parts.size >= 3) {
            val heroName = parts[0]
            val notes = parts[1]
            val rating = parts[2].toIntOrNull() ?: 3

            val repository = (application as TacticoreApplication).repository
            val hero = repository.getHeroes().find { it.name.equals(heroName, ignoreCase = true) }
            if (hero != null) {
                val build = HeroBuild(
                    heroId = hero.id,
                    userNotes = notes,
                    rating = rating
                )
                lifecycleScope.launch {
                    repository.saveBuild(build)
                    Toast.makeText(this@QRScannerActivity, "Билдът на $heroName е импортиран", Toast.LENGTH_LONG).show()
                    finish()
                }
            } else {
                Toast.makeText(this, "Герой '$heroName' не е намерен", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Невалиден QR формат", Toast.LENGTH_SHORT).show()
        }
    }
}