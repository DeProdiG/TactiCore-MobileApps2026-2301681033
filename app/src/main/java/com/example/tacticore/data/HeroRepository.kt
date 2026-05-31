package com.example.tacticore.data

import android.content.ContentValues
import android.content.Context
import com.example.tacticore.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HeroRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    // Актуален списък с всички герои от Overwatch
    fun getHeroes(): List<Hero> = listOf(
        Hero(
            1,
            "Tracer",
            "DPS",
            "Бившият агент на Overwatch, известен като \"Трейсър\", е пътуваща във времето приключенка и неудържима сила на доброто.",
            R.drawable.tracer
        ),
        Hero(
            2,
            "Reinhardt",
            "Tank",
            "Райнхардт Вилхелм се представя за шампион на отминала епоха, който се ръководи от рицарските кодекси за храброст, справедливост и смелост.",
            R.drawable.reinhardt
        ),
        Hero(
            3,
            "Mercy",
            "Support",
            "Ангел-пазител за тези, които попаднат под нейната грижа, д-р Анжела Циглер е ненадминат лечител, брилянтен учен и твърд защитник на мира.",
            R.drawable.mercy
        ),
        Hero(
            4,
            "Genji",
            "DPS",
            "Киборгът нинджа Генджи Шимада се е помирил с усиленото тяло, което някога е отхвърлял, и правейки това, е открил по-висша човечност.",
            R.drawable.genji
        ),
        Hero(
            5,
            "Winston",
            "Tank",
            "Супер-интелигентна, генетично модифицирана горила, Уинстън е брилянтен учен и шампион на човешкия потенциал.",
            R.drawable.winston
        ),
        Hero(
            6,
            "Ana",
            "Support",
            "Един от основателите на Overwatch, Ана използва уменията и опита си, за да защити дома си и хората, за които се грижи.",
            R.drawable.ana
        ),
        Hero(
            7,
            "Ashe",
            "DPS",
            "Лидерът на бандата \"Мъртвешките шерифи\" не се страхува да цапа ръцете си, но винаги действа по свой собствен начин.",
            R.drawable.ashe
        ),
        Hero(
            8,
            "Baptiste",
            "Support",
            "Елитен боен медик и бивш оперативен агент на Талон, Батист вече използва уменията си, за да помага на хората, чийто живот е бил засегнат от война.",
            R.drawable.baptiste
        ),
        Hero(
            9,
            "Bastion",
            "DPS",
            "Бивш фронтови боец в опустошителната Омнична криза, това любопитно устройство Бастион сега изследва света, очарован от природата, но предпазливо към уплашеното човечество.",
            R.drawable.bastion
        ),
        Hero(
            10,
            "Brigitte",
            "Support",
            "Бригита вече не стои настрана и е вдигнала оръжие, за да защитава онези, които се нуждаят от закрила.",
            R.drawable.brigitte
        ),
        Hero(
            11,
            "Cassidy",
            "DPS",
            "Въоръжен с револвера си \"Миротворец\", аутло̀тът Коул Касиди раздава справедливост според собствените си правила.",
            R.drawable.cassidy
        ),
        Hero(
            12,
            "D.Va",
            "Tank",
            "Бивша професионална геймърка, която сега използва уменията си, за да пилотира модерен мех в защита на родината си.",
            R.drawable.diva
        ),
        Hero(
            13,
            "Doomfist",
            "Tank",
            "Разумният лидер на терористичната организация Талон, която действа в пряка опозиция на Overwatch.",
            R.drawable.doomfist
        ),
        Hero(
            14,
            "Echo",
            "DPS",
            "Многофункционален, адаптивен робот с най-сложния изкуствен интелект в света, първоначално създаден за бойни мисии на Overwatch.",
            R.drawable.echo
        ),
        Hero(
            15,
            "Freja",
            "DPS",
            " Бивша оперативна служителка на \"Търсене и спасяване\", превърнала се в ловец на глави, Фрея може да намери всеки, стига цената да е подходяща.",
            R.drawable.freja
        ),
        Hero(
            16,
            "Hanzo",
            "DPS",
            "Усъвършенствайки уменията си на стрелец с лък и асинин, Хандзо Шимада се стреми да се докаже като воин без равен.",
            R.drawable.hanzo
        ),
        Hero(
            17,
            "Hazard",
            "Tank",
            "Радикален бунтар и непобедимо оръжие, Хазард бързо придобива слава в криминалния свят, водейки битка срещу установения ред.",
            R.drawable.hazard
        ),
        Hero(
            18,
            "Illari",
            "Support",
            "Като последна от войните на Инти, Илари е съд за слънчевата енергия. Тя ще направи всичко, за да изкупи миналото си.",
            R.drawable.illari
        ),
        Hero(
            19,
            "Junker Queen",
            "Tank",
            "Въоръжена с брадвата си \"Кървав клане\" и електромагнитен ръкавиц, безскрупулният лидер на Джънкерите има за мисия да управлява света.",
            R.drawable.junkerqueen
        ),
        Hero(
            20,
            "Junkrat",
            "DPS",
            "Джънкрат е демолиционер, обсебен от експлозиви, който живее, за да създава хаос и разрушения.",
            R.drawable.junkrat
        ),
        Hero(
            21,
            "Juno",
            "Support",
            "Първият човек, роден на Марс, Джуно използва своята космическа технология, за да разреши всеки проблем, който попадне в нейната орбита.",
            R.drawable.juno
        ),
        Hero(
            22,
            "Kiriko",
            "Support",
            "Мико на светилището Канезака и дъщеря на бившия майстор на меча на Шимада, Кирико Камори насочва както духовните си, така и нинджа уменията си, за да излекува разкъсания си град.",
            R.drawable.kiriko
        ),
        Hero(
            23,
            "Lifeweaver",
            "Support",
            "Брилянтният създател на биосветлина, технология, която слива растителна материя и твърда светлина. Той мечтае да излекува света.",
            R.drawable.lifeweaver
        ),
        Hero(
            24,
            "Lucio",
            "Support",
            "Интернационална знаменитост, която вдъхновява социални промени чрез своята музика и активизъм.",
            R.drawable.lucio
        ),
        Hero(
            25,
            "Mauga",
            "Tank",
            "Харизматичен и хитър самоански воин, Мауга вирее в хаоса на бойното поле и живее всеки ден, сякаш е последен.",
            R.drawable.mauga
        ),
        Hero(
            26,
            "Mei",
            "DPS",
            "Учен, който е поел борбата за опазване на околната среда в свои ръце.",
            R.drawable.mauga
        ),
        Hero(
            27,
            "Moira",
            "Support",
            "Едновременно брилянтна и противоречива, ученият Мойра О'Диорийн е на върха на генетичното инженерство, търсейки начин да пренапише градивните елементи на живота.",
            R.drawable.moira
        ),
        Hero(
            28,
            "Orisa",
            "Tank",
            "Създадена от части на един от краткотрайните защитни роботи на Нумбани, Ориса е най-новият защитник на града, въпреки че все още има много да учи.",
            R.drawable.orisa
        ),
        Hero(
            29,
            "Pharah",
            "DPS",
            "Ангажиментът на Фарайха Амари към дълга е в кръвта ѝ. Тя произхожда от дълга линия на високо уважавани войници и гори от желание да служи с чест.",
            R.drawable.pharah
        ),
        Hero(
            30,
            "Ramattra",
            "Tank",
            "Бруталният лидер на Нулт Сектор няма да спре пред нищо, за да бъде реализирана неговата визия за света.",
            R.drawable.rammatra
        ),
        Hero(
            31,
            "Reaper",
            "DPS",
            "Някои говорят за терорист в черна роба, известен просто като \"REAPER\". Неговата самоличност и мотиви са загадка. Това, което се знае, е, че там, където се появи, смъртта го следва.",
            R.drawable.reaper
        ),
        Hero(
            32,
            "Roadhog",
            "Tank",
            "Мощен закононарушител с добре заслужила репутация на безразборна и необуздана разруха.",
            R.drawable.roadhog
        ),
        Hero(
            33,
            "Sigma",
            "Tank",
            "Животът на брилянтния астрофизик д-р Сийбрен де Койпер се променя завинаги, когато неуспешен експеримент му дава способността да контролира гравитацията; сега Талон го манипулира за своите цели.",
            R.drawable.sigma
        ),
        Hero(
            34,
            "Sierra",
            "DPS",
            "Отдаден съотборник с подобрени способности, Сиера се е изкачила в йерархията на Хеликс Секюрити.",
            R.drawable.sierra
        ),
        Hero(
            35,
            "Sojourn",
            "DPS",
            "Като лидер в отминалите дни на Overwatch, Вивиан Чейс с псевдоним Соджърн е решена да гарантира, че новите ѝ герои няма да повторят грешките на миналото.",
            R.drawable.sojourn
        ),
        Hero(
            36,
            "Soldier:76",
            "DPS",
            "Цел на международно издирване, известният като \"Солджър: 76\" води лична война, за да разкрие истината зад разпадането на Overwatch.",
            R.drawable.soldier
        ),
        Hero(
            37,
            "Sombra",
            "DPS",
            "Един от най-известните хакери в света, Сомбра използва информацията, за да манипулира управляващите.",
            R.drawable.sombra
        ),
        Hero(
            38,
            "Symmetra",
            "DPS",
            "Симетра буквално променя реалността. Чрез манипулиране на конструкции от твърда светлина, тя създава света, какъвто тя желае, с надеждата да създаде едно перфектно общество.",
            R.drawable.symmetra
        ),
        Hero(
            39,
            "Torbjorn",
            "DPS",
            " В разцвета си Overwatch притежава едни от най-модерните оръжия на планетата, които могат да бъдат проследени до работилницата на гениалния инженер Торбьорн Линдхолм.",
            R.drawable.torbjorn
        ),
        Hero(
            40,
            "Widowmaker",
            "DPS",
            "Перфектният асинин; търпелив, безмилостен убиец, който не показва нито емоции, нито угризения на съвестта.",
            R.drawable.widowmaker
        ),
        Hero(
            41,
            "Wrecking Ball",
            "Tank",
            "Находчив и високо интелигентен механик и боец, Рекинг Бол се издига от лабораториите на Horizon Lunar Colony, за да стане шампион на Джуинкър Куин.",
            R.drawable.wreckingball
        ),
        Hero(
            42,
            "Zarya",
            "Tank",
            "дна от най-силните жени в света, известната спортистка Александра Зарянова жертва личната си слава, за да защити семейството, приятелите и страната си във военно време.",
            R.drawable.zarya
        ),
        Hero(
            43,
            "Zenyatta",
            "Support",
            "Омник монах, който броди по света в търсене на духовно просветление. Казват, че тези, които срещнат пътя му, никога повече не били същите.",
            R.drawable.zenyatta
        ),
        Hero(
            44,
            "Jetpack Cat",
            "Support",
            "Безстрашен котак-пилот, оборудван с най-новото изобретение на Бригита, Джетпак Кет носи очарователна въздушна подкрепа на бойното поле.",
            R.drawable.jetpackcat
        ),
        Hero(
            45,
            "Vendetta",
            "DPS",
            "Като шампион на Колосеото и член на италианския елит, Вендета няма да спре пред нищо, за да си върне трона на семейната престъпна империя.",
            R.drawable.vendetta
        ),
        Hero(
            46,
            "Venture",
            "DPS",
            "Пътуващ по света археолог и изследовател, Венчър има страст към разкриването на историческите мистерии",
            R.drawable.venture
        ),
        Hero(
            47,
            "Wuyang",
            "Support",
            "Владеещ лечителски технологии от Водния колеж на университета Вуксинг, Вуянг винаги е готов да обърне хода на битката.",
            R.drawable.wuyang
        ),
        Hero(
            48,
            "Mizuki",
            "Support",
            "Роден под сянката на нещастието, Мидзуки ще направи всичко, за да се освободи от проклятието си и да реши собствената си съдба.",
            R.drawable.mizuki
        ),
        Hero(
            49,
            "Emre",
            "DPS",
            "Емре изчезва след успешен престой в ударния отряд на Overwatch. Години по-късно той се появява отново, а тялото и умът му са манипулирани от нещо непознато.",
            R.drawable.emre
        ),
        Hero(
            50,
            "Domina",
            "Tank",
            "Наследница и вицепрезидент на Vishkar Industries, Домина притежава титанично богатство, което влага в усилията си да предефинира човешкия опит.",
            R.drawable.domina
        ),
        Hero(
            51,
            "Anran",
            "DPS",
            "Завършила с отличие Огнения колеж на университета Вуксинг, Анран винаги е готова да прекрачва границите си.",
            R.drawable.anran
        )
    )

    fun getHeroById(id: Int): Hero? = getHeroes().find { it.id == id }

    suspend fun getBuildForHero(heroId: Int, mode: String): HeroBuild? = withContext(Dispatchers.IO) {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_HERO_BUILDS,
            null,
            "${DatabaseHelper.COL_HERO_ID} = ? AND ${DatabaseHelper.COL_MODE} = ?",
            arrayOf(heroId.toString(), mode),
            null,
            null,
            null
        )
        val build = if (cursor.moveToFirst()) {
            HeroBuild(
                id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID)),
                heroId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HERO_ID)),
                mode = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MODE)),
                userNotes = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_NOTES)
                ),
                rating = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_RATING)),
                stadiumItems = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STADIUM_ITEMS)
                ),
                stadiumGadgets = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STADIUM_GADGETS)
                ),
                stadiumPower = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STADIUM_POWER)
                ),
                quickPlayPerks = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.COL_QUICKPLAY_PERKS)
                ),
                timestamp = cursor.getLong(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TIMESTAMP)
                )
            )
        } else {
            null
        }
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
            db.update(
                DatabaseHelper.TABLE_HERO_BUILDS,
                values,
                "${DatabaseHelper.COL_ID} = ?",
                arrayOf(build.id.toString())
            )
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
            null,
            null,
            "${DatabaseHelper.COL_TIMESTAMP} DESC"
        )
        val builds = mutableListOf<HeroBuild>()
        while (cursor.moveToNext()) {
            builds.add(
                HeroBuild(
                    id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID)),
                    heroId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HERO_ID)),
                    mode = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MODE)),
                    userNotes = cursor.getString(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_NOTES)
                    ),
                    rating = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_RATING)),
                    stadiumItems = cursor.getString(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STADIUM_ITEMS)
                    ),
                    stadiumGadgets = cursor.getString(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STADIUM_GADGETS)
                    ),
                    stadiumPower = cursor.getString(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STADIUM_POWER)
                    ),
                    quickPlayPerks = cursor.getString(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COL_QUICKPLAY_PERKS)
                    ),
                    timestamp = cursor.getLong(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TIMESTAMP)
                    )
                )
            )
        }
        cursor.close()
        db.close()
        builds
    }
    suspend fun getAllBuilds(): List<HeroBuild> = withContext(Dispatchers.IO) {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_HERO_BUILDS,
            null,
            null,
            null,
            null,
            null,
            "${DatabaseHelper.COL_TIMESTAMP} DESC"
        )
        val builds = mutableListOf<HeroBuild>()
        while (cursor.moveToNext()) {
            builds.add(
                HeroBuild(
                    id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID)),
                    heroId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HERO_ID)),
                    mode = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MODE)),
                    userNotes = cursor.getString(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_NOTES)
                    ),
                    rating = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_RATING)),
                    stadiumItems = cursor.getString(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STADIUM_ITEMS)
                    ),
                    stadiumGadgets = cursor.getString(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STADIUM_GADGETS)
                    ),
                    stadiumPower = cursor.getString(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STADIUM_POWER)
                    ),
                    quickPlayPerks = cursor.getString(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COL_QUICKPLAY_PERKS)
                    ),
                    timestamp = cursor.getLong(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TIMESTAMP)
                    )
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
        db.delete(
            DatabaseHelper.TABLE_HERO_BUILDS,
            "${DatabaseHelper.COL_ID} = ?",
            arrayOf(buildId.toString())
        )
        db.close()
    }
}
