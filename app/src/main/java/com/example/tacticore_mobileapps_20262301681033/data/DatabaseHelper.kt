package com.example.tacticore.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "tacticore.db"
        private const val DATABASE_VERSION = 2

        const val TABLE_HERO_BUILDS = "hero_builds"
        const val COL_ID = "id"
        const val COL_HERO_ID = "hero_id"
        const val COL_MODE = "mode"
        const val COL_USER_NOTES = "user_notes"
        const val COL_RATING = "rating"
        const val COL_STADIUM_ITEMS = "stadium_items"
        const val COL_STADIUM_GADGETS = "stadium_gadgets"
        const val COL_STADIUM_POWER = "stadium_power"
        const val COL_QUICKPLAY_PERKS = "quickplay_perks"
        const val COL_TIMESTAMP = "timestamp"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_HERO_BUILDS (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_HERO_ID INTEGER NOT NULL,
                $COL_MODE TEXT NOT NULL,
                $COL_USER_NOTES TEXT,
                $COL_RATING INTEGER NOT NULL,
                $COL_STADIUM_ITEMS TEXT,
                $COL_STADIUM_GADGETS TEXT,
                $COL_STADIUM_POWER TEXT,
                $COL_QUICKPLAY_PERKS TEXT,
                $COL_TIMESTAMP INTEGER NOT NULL
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_HERO_BUILDS")
        onCreate(db)
    }
}