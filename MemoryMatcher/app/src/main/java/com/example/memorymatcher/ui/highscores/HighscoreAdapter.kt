package com.example.memorymatcher.ui.highscores

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.memorymatcher.data.db.entity.Highscore


class HighscoreAdapter(
    private val highscores: List<Highscore>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(com.example.memorymatcher.R.layout.item_high_score, parent, false)
        return HighscoresViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return highscores.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HighscoresViewHolder) {
            holder.bindData(highscores[position])
        }
    }
}
