package com.example.tacticore.ui.qr

import android.graphics.Bitmap
import android.graphics.Color
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.example.tacticore.R
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

object QRGenerator {

    fun generateQRCode(data: String): Bitmap? {
        return try {
            val writer = QRCodeWriter()
            val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 400, 400)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val pixels = IntArray(width * height)
            for (y in 0 until height) {
                for (x in 0 until width) {
                    pixels[y * width + x] = if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE
                }
            }
            Bitmap.createBitmap(pixels, width, height, Bitmap.Config.RGB_565)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun showQRCodeDialog(context: android.content.Context, data: String) {
        val bitmap = generateQRCode(data)
        if (bitmap == null) return
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_qr, null)
        val imageView = dialogView.findViewById<ImageView>(R.id.qrImageView)
        imageView.setImageBitmap(bitmap)

        AlertDialog.Builder(context)
            .setTitle("QR код на билда")
            .setView(dialogView)
            .setPositiveButton("OK", null)
            .show()
    }
}