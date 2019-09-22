package com.example.memorymatcher.data.model


data class Card(
    val id: Long,
    val src: String,
    var cardStatus: CardStatus = CardStatus.DOWN
)

