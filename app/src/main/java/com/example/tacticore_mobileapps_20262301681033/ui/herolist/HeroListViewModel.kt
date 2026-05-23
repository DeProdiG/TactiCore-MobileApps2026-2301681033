package com.example.tacticore.ui.herolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tacticore.data.Hero
import com.example.tacticore.data.HeroRepository

class HeroListViewModel(private val repository: HeroRepository) : ViewModel() {

    private val _heroes = MutableLiveData<List<Hero>>()
    val heroes: LiveData<List<Hero>> = _heroes

    init {
        _heroes.value = repository.getHeroes()
    }
}