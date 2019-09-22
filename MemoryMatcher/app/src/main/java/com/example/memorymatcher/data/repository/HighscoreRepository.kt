package com.example.memorymatcher.data.repository

import com.example.memorymatcher.data.db.HighScoreDatabase
import com.example.memorymatcher.data.db.entity.Highscore
import com.example.memorymatcher.util.Coroutines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HighscoreRepository(
    private val highScoreDatabase: HighScoreDatabase
) {

    suspend fun getHighscoresFromDatabase(): List<Highscore> {
        return withContext(Dispatchers.IO) {
            highScoreDatabase.getHighscoreDao().getHighscoreList()
        }
    }

    fun saveHighscoreToDatabase(score: Highscore) {
        Coroutines.io {
            highScoreDatabase.getHighscoreDao().insertHighScore(score)
        }
    }
}