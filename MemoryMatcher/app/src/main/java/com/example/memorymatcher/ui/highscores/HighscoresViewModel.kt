package com.example.memorymatcher.ui.highscores

import androidx.lifecycle.ViewModel
import com.example.memorymatcher.data.db.entity.Highscore
import com.example.memorymatcher.data.repository.HighscoreRepository
import com.example.memorymatcher.util.lazyDeferred

class HighscoresViewModel(
    private val highscoreRepository: HighscoreRepository
) : ViewModel() {

    val highscores by lazyDeferred {
        highscoreRepository.getHighscoresFromDatabase()
    }

    suspend fun getHighscores(): List<Highscore> {
        val scores = highscores.await()
        return scores.sortedByDescending { it.score }
    }
}