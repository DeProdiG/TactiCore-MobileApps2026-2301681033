package com.example.tacticore_mobileapps_20262301681033.ui.herodetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tacticore_mobileapps_20262301681033.data.Hero
import com.example.tacticore_mobileapps_20262301681033.data.HeroBuild
import com.example.tacticore_mobileapps_20262301681033.data.HeroRepository
import kotlinx.coroutines.launch

class HeroDetailViewModel : ViewModel() {

    private lateinit var repository: HeroRepository
    private var heroId: Int = 0
    private var currentMode: String = "stadium"

    private val _hero = MutableLiveData<Hero?>()
    val hero: LiveData<Hero?> = _hero

    private val _build = MutableLiveData<HeroBuild?>()
    val build: LiveData<HeroBuild?> = _build

    fun init(repository: HeroRepository, heroId: Int, mode: String) {
        this.repository = repository
        this.heroId = heroId
        this.currentMode = mode
        loadHero()
        loadBuild()
    }

    private fun loadHero() {
        _hero.value = repository.getHeroById(heroId)
    }

    private fun loadBuild() {
        viewModelScope.launch {
            val savedBuild = repository.getBuildForHero(heroId, currentMode)
            _build.postValue(savedBuild)
        }
    }

    fun saveBuild(
        notes: String,
        rating: Int,
        stadiumItems: String? = null,
        stadiumGadgets: String? = null,
        stadiumPower: String? = null,
        quickPlayPerks: String? = null
    ) {
        viewModelScope.launch {
            val existing = _build.value
            val build = HeroBuild(
                id = existing?.id ?: 0,
                heroId = heroId,
                mode = currentMode,
                userNotes = notes,
                rating = rating,
                stadiumItems = stadiumItems,
                stadiumGadgets = stadiumGadgets,
                stadiumPower = stadiumPower,
                quickPlayPerks = quickPlayPerks
            )
            repository.saveBuild(build)
            _build.postValue(build)
        }
    }

    fun deleteBuild() {
        viewModelScope.launch {
            repository.deleteBuild(heroId, currentMode)
            _build.postValue(null)
        }
    }

    fun setMode(mode: String) {
        currentMode = mode
        loadBuild()
    }
}
