package com.example.memorymatcher.ui.highscores

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.memorymatcher.data.db.entity.Highscore
import kotlinx.android.synthetic.main.item_high_score.view.*

class HighscoresViewHolder(
    view: View
) : RecyclerView.ViewHolder(view) {

    fun bindData(highscore: Highscore) {
        itemView.run {
            item_score.text = highscore.score.toString()
        }
    }
}
