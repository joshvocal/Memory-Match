package com.example.memorymatcher.ui.highscores

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.memorymatcher.data.repository.HighscoreRepository

class HighscoresViewModelFactory(
    private val highscoreRepository: HighscoreRepository
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HighscoresViewModel(highscoreRepository) as T
    }
}