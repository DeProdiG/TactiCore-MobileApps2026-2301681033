package com.example.tacticore.data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import java.security.MessageDigest

class AuthRepository(private val context: Context) {

    private val dbHelper = DatabaseHelper(context)

    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun register(username: String, password: String): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COL_USERNAME, username)
            put(DatabaseHelper.COL_PASSWORD_HASH, hashPassword(password))
        }
        val result = db.insert(DatabaseHelper.TABLE_USERS, null, values)
        db.close()
        return result != -1L
    }

    fun login(username: String, password: String): Long {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_USERS,
            arrayOf(DatabaseHelper.COL_USER_ID),
            "${DatabaseHelper.COL_USERNAME} = ? AND ${DatabaseHelper.COL_PASSWORD_HASH} = ?",
            arrayOf(username, hashPassword(password)),
            null, null, null
        )
        val userId = if (cursor.moveToFirst()) cursor.getLong(0) else -1L
        cursor.close()
        db.close()
        return userId
    }

    fun getCurrentUserId(): Long {
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        return prefs.getLong("user_id", -1)
    }
    fun getUsername(userId: Long): String? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_USERS,
            arrayOf(DatabaseHelper.COL_USERNAME),
            "${DatabaseHelper.COL_USER_ID} = ?",
            arrayOf(userId.toString()),
            null, null, null
        )
        val username = if (cursor.moveToFirst()) cursor.getString(0) else null
        cursor.close()
        db.close()
        return username
    }
    @SuppressLint("UseKtx")
    fun saveLoggedInUser(userId: Long) {
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        prefs.edit().putLong("user_id", userId).apply()
    }

    @SuppressLint("UseKtx")
    fun logout() {
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        prefs.edit().remove("user_id").apply()
    }
}