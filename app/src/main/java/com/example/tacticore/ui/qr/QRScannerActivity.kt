package com.example.tacticore.ui.qr

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
        if (parts.size >= 4) {
            val heroName = parts[0]
            val mode = parts[1].lowercase()
            val notes = parts[2]
            val rating = parts[3].toIntOrNull() ?: 3

            var stadiumItems: String? = null
            var stadiumGadgets: String? = null
            var stadiumPower: String? = null
            var quickPlayPerks: String? = null

            if (mode == "stadium") {
                if (parts.size >= 5) stadiumItems = parts[4].replace("Items:", "")
                if (parts.size >= 6) stadiumGadgets = parts[5].replace("Gadgets:", "")
                if (parts.size >= 7) stadiumPower = parts[6].replace("Power:", "")
            } else if (mode == "quickplay") {
                if (parts.size >= 5) quickPlayPerks = parts[4].replace("Perks:", "")
            }

            val repository = (application as TacticoreApplication).repository
            val hero = repository.getHeroes().find { it.name.equals(heroName, ignoreCase = true) }
            if (hero != null) {
                val build = HeroBuild(
                    heroId = hero.id,
                    mode = mode,
                    userNotes = notes,
                    rating = rating,
                    stadiumItems = stadiumItems,
                    stadiumGadgets = stadiumGadgets,
                    stadiumPower = stadiumPower,
                    quickPlayPerks = quickPlayPerks
                )
                lifecycleScope.launch {
                    repository.saveBuild(build)
                    Toast.makeText(
                        this@QRScannerActivity,
                        "Билдът на $heroName ($mode) е импортиран",
                        Toast.LENGTH_LONG
                    ).show()
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
