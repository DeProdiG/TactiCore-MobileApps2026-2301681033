package com.example.tacticore.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class HeroRepository(private val context: Context) {

    private val dbHelper = DatabaseHelper(context)

    // 1. Статични данни за героите (Overwatch)
    fun getHeroes(): List<Hero> = listOf(
        Hero(
            id = 1,
            name = "Tracer",
            role = "DPS",
            description = "Бърза атакуваща героиня с способност да се връща във времето.",
            imageResId = android.R.drawable.ic_menu_camera
        ),
        Hero(
            id = 2,
            name = "Reinhardt",
            role = "Tank",
            description = "Лидер с голямо ковано чук и бариера.",
            imageResId = android.R.drawable.ic_menu_gallery
        ),
        Hero(
            id = 3,
            name = "Mercy",
            role = "Support",
            description = "Целител, който може да възкресява съюзници.",
            imageResId = android.R.drawable.ic_menu_help
        ),
        Hero(
            id = 4,
            name = "Genji",
            role = "DPS",
            description = "Нинджа с шурикени и отразяване на снаряди.",
            imageResId = android.R.drawable.ic_menu_manage
        ),
        Hero(
            id = 5,
            name = "Winston",
            role = "Tank",
            description = "Учен маймуна, който скача върху враговете.",
            imageResId = android.R.drawable.ic_menu_edit
        )
    )

    fun getHeroById(id: Int): Hero? = getHeroes().find { it.id == id }

    // 2. Четене на билд за даден герой (като Flow)
    fun getBuildForHero(heroId: Int): Flow<HeroBuild?> = flow {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_HERO_BUILDS,
            null,
            "${DatabaseHelper.COL_HERO_ID} = ?",
            arrayOf(heroId.toString()),
            null, null, null
        )

        val build = if (cursor.moveToFirst()) {
            HeroBuild(
                id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID)),
                heroId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HERO_ID)),
                userNotes = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_NOTES)),
                rating = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_RATING)),
                timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TIMESTAMP))
            )
        } else {
            null
        }
        cursor.close()
        db.close()
        emit(build)
    }

    // 3. Запазване (Create или Update) на билд
    suspend fun saveBuild(build: HeroBuild) = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COL_HERO_ID, build.heroId)
            put(DatabaseHelper.COL_USER_NOTES, build.userNotes)
            put(DatabaseHelper.COL_RATING, build.rating)
            put(DatabaseHelper.COL_TIMESTAMP, build.timestamp)
        }

        if (build.id == 0L) {
            // Insert нов запис
            db.insert(DatabaseHelper.TABLE_HERO_BUILDS, null, values)
        } else {
            // Update съществуващ запис
            db.update(
                DatabaseHelper.TABLE_HERO_BUILDS,
                values,
                "${DatabaseHelper.COL_ID} = ?",
                arrayOf(build.id.toString())
            )
        }
        db.close()
    }

    // 4. Изтриване на билд по heroId
    suspend fun deleteBuildByHeroId(heroId: Int) = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        db.delete(
            DatabaseHelper.TABLE_HERO_BUILDS,
            "${DatabaseHelper.COL_HERO_ID} = ?",
            arrayOf(heroId.toString())
        )
        db.close()
    }
}