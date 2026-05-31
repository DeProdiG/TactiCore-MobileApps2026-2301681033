package com.example.tacticore.ui.herolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tacticore.data.Hero
import com.example.tacticore.data.HeroRepository

class HeroListViewModel(private val repository: HeroRepository) : ViewModel() {

    private val _allHeroes = MutableLiveData<List<Hero>>()
    val allHeroes: LiveData<List<Hero>> = _allHeroes

    private val _filteredHeroes = MutableLiveData<List<Hero>>()
    val filteredHeroes: LiveData<List<Hero>> = _filteredHeroes

    private var currentQuery = ""
    private var currentRole = "All"

    init {
        loadHeroes()
    }

    private fun loadHeroes() {
        _allHeroes.value = repository.getHeroes()
        applyFilters()
    }

    fun setSearchQuery(query: String) {
        currentQuery = query.trim().lowercase()
        applyFilters()
    }

    fun setRoleFilter(role: String) {
        currentRole = role
        applyFilters()
    }

    private fun applyFilters() {
        val heroes = _allHeroes.value ?: return
        val filtered = heroes.filter { hero ->
            (currentRole == "All" || hero.role.equals(currentRole, ignoreCase = true)) &&
                (currentQuery.isEmpty() || hero.name.lowercase().contains(currentQuery))
        }
        _filteredHeroes.value = filtered
    }
}
