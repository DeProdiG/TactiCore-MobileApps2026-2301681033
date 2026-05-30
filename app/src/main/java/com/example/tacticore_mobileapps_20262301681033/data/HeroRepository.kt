package com.example.tacticore.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.tacticore.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class HeroRepository(private val context: Context) {

    private val dbHelper = DatabaseHelper(context)

    // Актуален списък с всички герои от Overwatch
    fun getHeroes(): List<Hero> = listOf(
        Hero(1, "Tracer", "DPS", "Бърза атакуваща героиня с възможност за връщане във времето.", R.drawable.tracer),
        Hero(2, "Reinhardt", "Tank", "Лидер с голямо ковано чук и бариера.", R.drawable.reinhardt),
        Hero(3, "Mercy", "Support", "Целител, който може да възкресява съюзници.", R.drawable.mercy),
        Hero(4, "Genji", "DPS", "Нинджа с шурикени и отразяване на снаряди.", R.drawable.genji),
        Hero(5, "Winston", "Tank", "Учен маймуна, който скача върху враговете.", R.drawable.winston),
        Hero(6, "Ana", "Support", "Снайперист-целител с биотични изстрели.", R.drawable.ana),
        Hero(7, "Ashe", "DPS", "Лидер на банда с пушка и динамит.", R.drawable.ashe),
        Hero(8, "Baptiste", "Support", "Боен медик с амплификатор и имунно поле.", R.drawable.baptiste),
        Hero(9, "Bastion", "DPS", "Трансформиращ се робот с картечница.", R.drawable.bastion),
        Hero(10, "Brigitte", "Support", "Ремонтник с броня и щит.", R.drawable.brigitte),
        Hero(11, "Cassidy", "DPS", "Ковбой с револвер и търкалящ се удар.", R.drawable.cassidy),
        Hero(12, "D.Va", "Tank", "Пилот на мех, който може да се самоунищожи.", R.drawable.diva),
        Hero(13, "Doomfist", "Tank", "Боец със сила, която разрушава земята.", R.drawable.doomfist),
        Hero(14, "Echo", "DPS", "Адаптивен андроид, копиращ врагове.", R.drawable.echo),
        Hero(15, "Freja", "DPS", "Нов герой (в процес на дефиниране).", R.drawable.freja),
        Hero(16, "Hanzo", "DPS", "Самурай с лък и драконова стрела.", R.drawable.hanzo),
        Hero(17, "Hazard", "Tank", "No description available.", R.drawable.hazard),
        Hero(18, "Illari", "Support", "Слънчева войн, която лекува с прасковен кристал.", R.drawable.illari),
        Hero(19, "Junker Queen", "Tank", "Кралица на скрапта с тежко брадва.", R.drawable.junkerqueen),
        Hero(20, "Junkrat", "DPS", "Анархист с експлозиви и капан.", R.drawable.junkrat),
        Hero(21, "Juno", "Support", "Лунен агент със способности за подкрепа.", R.drawable.juno),
        Hero(22, "Kiriko", "Support", "Целител-куноич, който хвърля защитни руни.", R.drawable.kiriko),
        Hero(23, "Lifeweaver", "Support", "Природен лечител с контрол над биотата.", R.drawable.lifeweaver),
        Hero(24, "Lucio", "Support", "DJ, който лекува с музика и усилва скоростта.", R.drawable.lucio),
        Hero(25, "Mauga", "Tank", "Маори войн с тежко копие.", R.drawable.mauga),
        Hero(26, "Mei", "DPS", "Климатолог, който замразява врагове.", R.drawable.mauga),
        Hero(27, "Moira", "Support", "Генетик с биотични ръце и изтощаване.", R.drawable.moira),
        Hero(28, "Orisa", "Tank", "Защитен робот с джиро-кула.", R.drawable.orisa),
        Hero(29, "Pharah", "DPS", "Ракетен войн с летателен костюм.", R.drawable.pharah),
        Hero(30, "Ramattra", "Tank", "Омник лидер с двоен режим на битка.", R.drawable.rammatra),
        Hero(31, "Reaper", "DPS", "Възкръснал убиец с двата шотгъна.", R.drawable.reaper),
        Hero(32, "Roadhog", "Tank", "Тежък месар с кука за издърпване.", R.drawable.roadhog),
        Hero(33, "Sigma", "Tank", "Астрофизик, който контролира гравитацията.", R.drawable.sigma),
        Hero(34, "Sierra", "DPS", "No description available.", R.drawable.sierra),
        Hero(35, "Sojourn", "DPS", "Кибернетичен войн със снайперска пушка.", R.drawable.sojourn),
        Hero(36, "Soldier:76", "DPS", "Ветеран с тактически прицел.", R.drawable.soldier),
        Hero(37, "Sombra", "DPS", "Хакер, който деактивира способности.", R.drawable.sombra),
        Hero(38, "Symmetra", "DPS", "Архитект, който строи турели и телепортери.", R.drawable.symmetra),
        Hero(39, "Torbjorn", "DPS", "Инженер с кула и ковач.", R.drawable.torbjorn),
        Hero(40, "Widowmaker", "DPS", "Снайперист с токсичен капан.", R.drawable.widowmaker),
        Hero(41, "Wrecking Ball", "Tank", "Хамстер в роботизирана сфера.", R.drawable.wreckingball),
        Hero(42, "Zarya", "Tank", "Тежка атлетка с лазер и бариери.", R.drawable.zarya),
        Hero(43, "Zenyatta", "Support", "Монах, който лечи и усилва щетите.", R.drawable.zenyatta),

        // Евентуално допълнителни, споменати с правописни грешки/алтернативни имена
        Hero(44, "Jetpack Cat", "Support", "Предполагаем херой-котка с раница.", R.drawable.jetpackcat),
        Hero(45, "Vendetta", "DPS", "Probably a typo for Orisa/Reaper? Will be removed later.", R.drawable.vendetta),
        Hero(46, "Venture", "DPS", "No info.", R.drawable.venture),
        Hero(47, "Wuyang", "Support", "No info.", R.drawable.wuyang),
        Hero(48, "Mizuki", "Support", "No info.", R.drawable.mizuki),
        Hero(49, "Emre", "DPS", "No info.", R.drawable.emre),
        Hero(50, "Domina", "Tank", "No info.", R.drawable.domina),
        Hero(51, "Anran", "DPS", "No info.", R.drawable.anran),
    )

    fun getHeroById(id: Int): Hero? = getHeroes().find { it.id == id }

    suspend fun getBuildForHero(heroId: Int, mode: String): HeroBuild? = withContext(Dispatchers.IO) {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_HERO_BUILDS,
            null,
            "${DatabaseHelper.COL_HERO_ID} = ? AND ${DatabaseHelper.COL_MODE} = ?",
            arrayOf(heroId.toString(), mode),
            null, null, null
        )
        val build = if (cursor.moveToFirst()) {
            HeroBuild(
                id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID)),
                heroId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HERO_ID)),
                mode = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MODE)),
                userNotes = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_NOTES)),
                rating = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_RATING)),
                stadiumItems = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STADIUM_ITEMS)),
                stadiumGadgets = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STADIUM_GADGETS)),
                stadiumPower = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STADIUM_POWER)),
                quickPlayPerks = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_QUICKPLAY_PERKS)),
                timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TIMESTAMP))
            )
        } else null
        cursor.close()
        db.close()
        return@withContext build
    }

    suspend fun saveBuild(build: HeroBuild) = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COL_HERO_ID, build.heroId)
            put(DatabaseHelper.COL_MODE, build.mode)
            put(DatabaseHelper.COL_USER_NOTES, build.userNotes)
            put(DatabaseHelper.COL_RATING, build.rating)
            put(DatabaseHelper.COL_STADIUM_ITEMS, build.stadiumItems)
            put(DatabaseHelper.COL_STADIUM_GADGETS, build.stadiumGadgets)
            put(DatabaseHelper.COL_STADIUM_POWER, build.stadiumPower)
            put(DatabaseHelper.COL_QUICKPLAY_PERKS, build.quickPlayPerks)
            put(DatabaseHelper.COL_TIMESTAMP, build.timestamp)
        }
        if (build.id == 0L) {
            db.insert(DatabaseHelper.TABLE_HERO_BUILDS, null, values)
        } else {
            db.update(DatabaseHelper.TABLE_HERO_BUILDS, values, "${DatabaseHelper.COL_ID} = ?", arrayOf(build.id.toString()))
        }
        db.close()
    }
    suspend fun getAllBuildsForHero(heroId: Int): List<HeroBuild> = withContext(Dispatchers.IO) {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_HERO_BUILDS,
            null,
            "${DatabaseHelper.COL_HERO_ID} = ?",
            arrayOf(heroId.toString()),
            null, null, "${DatabaseHelper.COL_TIMESTAMP} DESC"
        )
        val builds = mutableListOf<HeroBuild>()
        while (cursor.moveToNext()) {
            builds.add(
                HeroBuild(
                    id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID)),
                    heroId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HERO_ID)),
                    mode = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MODE)),
                    userNotes = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_NOTES)),
                    rating = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_RATING)),
                    stadiumItems = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STADIUM_ITEMS)),
                    stadiumGadgets = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STADIUM_GADGETS)),
                    stadiumPower = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STADIUM_POWER)),
                    quickPlayPerks = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_QUICKPLAY_PERKS)),
                    timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TIMESTAMP))
                )
            )
        }
        cursor.close()
        db.close()
        builds
    }
    suspend fun deleteBuild(heroId: Int, mode: String) = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        db.delete(
            DatabaseHelper.TABLE_HERO_BUILDS,
            "${DatabaseHelper.COL_HERO_ID} = ? AND ${DatabaseHelper.COL_MODE} = ?",
            arrayOf(heroId.toString(), mode)
        )
        db.close()
    }
    suspend fun deleteBuildById(buildId: Long) = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        db.delete(DatabaseHelper.TABLE_HERO_BUILDS, "${DatabaseHelper.COL_ID} = ?", arrayOf(buildId.toString()))
        db.close()
    }
}