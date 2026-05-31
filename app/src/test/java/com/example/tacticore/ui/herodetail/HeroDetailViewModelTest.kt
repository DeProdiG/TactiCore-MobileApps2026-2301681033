package com.example.tacticore.ui.herodetail // проверете дали пакетът съвпада

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.tacticore.data.Hero
import com.example.tacticore.data.HeroBuild
import com.example.tacticore.data.HeroRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.*
import org.mockito.kotlin.*

@ExperimentalCoroutinesApi
class HeroDetailViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: HeroRepository
    private lateinit var viewModel: HeroDetailViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        repository = mock()
        viewModel = HeroDetailViewModel() // без параметри
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init and loadHero sets hero and build`() = runTest {
        val hero = Hero(1, "Tracer", "DPS", "desc", android.R.drawable.ic_menu_camera)
        val build = HeroBuild(heroId = 1, mode = "stadium", userNotes = "notes", rating = 5)
        whenever(repository.getHeroById(1)).thenReturn(hero)
        whenever(repository.getBuildForHero(1, "stadium")).thenReturn(build)

        viewModel.init(repository, 1, "stadium")

        assertEquals(hero, viewModel.hero.value)
        advanceUntilIdle()
        assertEquals(build, viewModel.build.value)
    }

    @Test
    fun `saveBuild calls repository saveBuild`() = runTest {
        viewModel.init(repository, 1, "stadium")
        viewModel.saveBuild("my notes", 4)
        advanceUntilIdle()

        verify(repository).saveBuild(any())
    }

    @Test
    fun `deleteBuild calls repository deleteBuild`() = runTest {
        viewModel.init(repository, 1, "stadium")
        viewModel.deleteBuild()
        advanceUntilIdle()

        verify(repository).deleteBuild(1, "stadium")
    }
}
