package com.example.memorymatcher.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.memorymatcher.data.db.dao.HighscoreDao
import com.example.memorymatcher.data.db.entity.Highscore

@Database(
    entities = [Highscore::class],
    version = 1,
    exportSchema = false
)
abstract class HighScoreDatabase : RoomDatabase() {

    abstract fun getHighscoreDao(): HighscoreDao

    companion object {
        @Volatile
        private var instance: HighScoreDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance
            ?: synchronized(LOCK) {
                instance
                    ?: buildDatabase(context).also { instance = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                HighScoreDatabase::class.java,
                "highscores.db"
            ).build()
    }
}