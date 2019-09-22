package com.example.memorymatcher.ui.game

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.memorymatcher.data.model.Card
import com.example.memorymatcher.data.model.CardStatus


class CardAdapter(
    private val cards: List<Card>,
    private val onCardClicked: ((Card, Int) -> Unit)
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // Allows recycler view items to be clickable
    private var isClickable = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(com.example.memorymatcher.R.layout.item_product, parent, false)
        return CardViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return cards.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CardViewHolder) {
            holder.bindData(cards[position], onCardClicked, isClickable)
        }
    }

    /**
     * This shows the image of the card
     */
    fun flipCardUp(cardPosition: Int) {
        cards[cardPosition].apply {
            cardStatus = CardStatus.UP
        }

        notifyItemChanged(cardPosition)
    }

    /**
     * This hides the image of the card
     */
    fun flipCardsDown() {
        for (card in cards) {
            if (card.cardStatus != CardStatus.FOUND) {
                card.cardStatus = CardStatus.DOWN
            }
        }

        notifyDataSetChanged()
    }

    /**
     * This shows the image of the card
     */
    fun setCardsFound() {
        for (card in cards) {
            if (card.cardStatus == CardStatus.UP) {
                card.cardStatus = CardStatus.FOUND
            }
        }

        notifyDataSetChanged()
    }

    /**
     * Allows the user to click on the recycler view items
     */
    fun enableClicks() {
        isClickable = true
        notifyDataSetChanged()
    }

    /**
     * Prevents the user from clicking on the recycler view items
     */
    fun disableClicks() {
        isClickable = false
        notifyDataSetChanged()
    }
}
