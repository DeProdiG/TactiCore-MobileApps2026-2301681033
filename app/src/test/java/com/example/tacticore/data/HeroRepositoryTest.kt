package com.example.tacticore.data

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [30])
class HeroRepositoryTest {

    private lateinit var repository: HeroRepository
    private lateinit var dbHelper: DatabaseHelper

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dbHelper = DatabaseHelper(context)
        repository = HeroRepository(context, 1L)
        dbHelper.writableDatabase.execSQL("DELETE FROM ${DatabaseHelper.TABLE_HERO_BUILDS}")
    }

    @After
    fun tearDown() {
        dbHelper.close()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testSaveAndGetBuild() = runTest {
        val build = HeroBuild(heroId = 1, mode = "stadium", userNotes = "test", rating = 5)
        repository.saveBuild(build)

        val retrieved = repository.getBuildForHero(heroId = 1, mode = "stadium")
        assertNotNull(retrieved)
        assertEquals("test", retrieved?.userNotes)
        assertEquals(5, retrieved?.rating)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testDeleteBuild() = runTest {
        val build = HeroBuild(heroId = 1, mode = "stadium", userNotes = "test", rating = 5)
        repository.saveBuild(build)
        repository.deleteBuild(heroId = 1, mode = "stadium")

        val retrieved = repository.getBuildForHero(heroId = 1, mode = "stadium")
        assertNull(retrieved)
    }

    @Test
    fun testGetHeroesReturnsNonEmpty() {
        val heroes = repository.getHeroes()
        assertTrue(heroes.isNotEmpty())
    }
}
