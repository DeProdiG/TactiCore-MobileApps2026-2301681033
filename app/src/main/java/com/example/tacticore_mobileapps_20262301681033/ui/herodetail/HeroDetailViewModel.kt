package com.example.tacticore.ui.herodetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tacticore.data.Hero
import com.example.tacticore.data.HeroBuild
import com.example.tacticore.data.HeroRepository
import kotlinx.coroutines.launch

class HeroDetailViewModel(private val repository: HeroRepository) : ViewModel() {

    private val _hero = MutableLiveData<Hero?>()
    val hero: LiveData<Hero?> = _hero

    private val _build = MutableLiveData<HeroBuild?>()
    val build: LiveData<HeroBuild?> = _build

    fun loadHero(heroId: Int) {
        _hero.value = repository.getHeroById(heroId)
        viewModelScope.launch {
            repository.getBuildForHero(heroId).collect { build ->
                _build.postValue(build)
            }
        }
    }

    fun saveBuild(heroId: Int, notes: String, rating: Int) {
        viewModelScope.launch {
            val existing = _build.value
            val build = HeroBuild(
                id = existing?.id ?: 0,
                heroId = heroId,
                userNotes = notes,
                rating = rating
            )
            repository.saveBuild(build)
        }
    }

    fun deleteBuild(heroId: Int) {
        viewModelScope.launch {
            repository.deleteBuildByHeroId(heroId)
            _build.postValue(null)
        }
    }
}