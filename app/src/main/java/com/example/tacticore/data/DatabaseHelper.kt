package com.example.tacticore.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "tacticore.db"
        private const val DATABASE_VERSION = 3

        const val TABLE_USERS = "users"
        const val COL_USER_ID = "user_id"
        const val COL_USERNAME = "username"
        const val COL_PASSWORD_HASH = "password_hash"

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
        db.execSQL("PRAGMA foreign_keys = ON;")
        val createUsersTable = """
            CREATE TABLE $TABLE_USERS (
                $COL_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_USERNAME TEXT UNIQUE NOT NULL,
                $COL_PASSWORD_HASH TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(createUsersTable)
        val createBuildsTable = """
            CREATE TABLE $TABLE_HERO_BUILDS (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_USER_ID INTEGER NOT NULL,
                $COL_HERO_ID INTEGER NOT NULL,
                $COL_MODE TEXT NOT NULL,
                $COL_USER_NOTES TEXT,
                $COL_RATING INTEGER NOT NULL,
                $COL_STADIUM_ITEMS TEXT,
                $COL_STADIUM_GADGETS TEXT,
                $COL_STADIUM_POWER TEXT,
                $COL_QUICKPLAY_PERKS TEXT,
                $COL_TIMESTAMP INTEGER NOT NULL,
                FOREIGN KEY($COL_USER_ID) REFERENCES $TABLE_USERS($COL_USER_ID) ON DELETE CASCADE
            )
        """.trimIndent()
        db.execSQL(createBuildsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_HERO_BUILDS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }
}