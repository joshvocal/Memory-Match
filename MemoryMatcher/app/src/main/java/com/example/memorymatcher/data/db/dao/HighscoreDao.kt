package com.example.memorymatcher.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.memorymatcher.data.db.entity.Highscore

@Dao
interface HighscoreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHighScore(highscore: Highscore)

    @Query("SELECT * FROM Highscore")
    fun getHighscoreList(): List<Highscore>
}