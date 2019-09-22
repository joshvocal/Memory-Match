package com.example.memorymatcher.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.memorymatcher.data.provider.GameSettingProvider
import com.example.memorymatcher.data.repository.HighscoreRepository
import com.example.memorymatcher.data.repository.ProductRepository

class GameViewModelFactory(
    private val productRepository: ProductRepository,
    private val highscoreRepository: HighscoreRepository,
    private val gameSettingProvider: GameSettingProvider
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GameViewModel(productRepository, highscoreRepository, gameSettingProvider) as T
    }
}