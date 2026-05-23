package com.example.tacticore.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "tacticore.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_HERO_BUILDS = "hero_builds"
        const val COL_ID = "id"
        const val COL_HERO_ID = "hero_id"
        const val COL_USER_NOTES = "user_notes"
        const val COL_RATING = "rating"
        const val COL_TIMESTAMP = "timestamp"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_HERO_BUILDS (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_HERO_ID INTEGER NOT NULL,
                $COL_USER_NOTES TEXT,
                $COL_RATING INTEGER NOT NULL,
                $COL_TIMESTAMP INTEGER NOT NULL
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Просто изтриваме и пресъздаваме при ъпгрейд (за опростяване)
        db.execSQL("DROP TABLE IF EXISTS $TABLE_HERO_BUILDS")
        onCreate(db)
    }
}