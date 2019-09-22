package com.example.memorymatcher.ui.game

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.memorymatcher.data.model.Card
import com.example.memorymatcher.data.model.CardStatus
import kotlinx.android.synthetic.main.item_product.view.*

class CardViewHolder(
    view: View
) : RecyclerView.ViewHolder(view) {

    /**
     * Binds the data to the view holder
     *
     * CardStatus.DOWN -> Hide the product image
     * CardStatus.UP -> Show the product image opaque
     * CardStatus.FOUND -> Show the product image and make it slightly transparent
     */
    fun bindData(card: Card, onCardClicked: ((Card, Int) -> Unit), isClickable: Boolean) {
        when {
            card.cardStatus == CardStatus.DOWN -> {
                itemView.run {
                    card.src.let {
                        Glide.with(context)
                            .clear(item_imageView)
                    }
                }

                itemView.alpha = 1.0F
            }
            card.cardStatus == CardStatus.UP -> {
                itemView.run {
                    card.src.let {
                        Glide.with(context)
                            .load(it)
                            .into(item_imageView)
                    }
                }

                itemView.alpha = 1.0F
            }
            card.cardStatus == CardStatus.FOUND -> {
                itemView.run {
                    card.src.let {
                        Glide.with(context)
                            .load(it)
                            .into(item_imageView)
                    }
                }

                itemView.alpha = 0.8F
            }
        }

        if (isClickable) {
            itemView.setOnClickListener {
                onCardClicked.invoke(card, adapterPosition)
            }
        }

    }
}
