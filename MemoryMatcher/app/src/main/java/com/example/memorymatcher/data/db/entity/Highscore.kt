package com.example.memorymatcher.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Highscore(
    val score: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
