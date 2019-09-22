package com.example.memorymatcher.ui.game

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.memorymatcher.data.db.entity.Highscore
import com.example.memorymatcher.data.db.entity.Product
import com.example.memorymatcher.data.model.Card
import com.example.memorymatcher.data.model.CardStatus
import com.example.memorymatcher.data.provider.GameSettingProvider
import com.example.memorymatcher.data.repository.HighscoreRepository
import com.example.memorymatcher.data.repository.ProductRepository
import com.example.memorymatcher.util.lazyDeferred
import kotlinx.coroutines.Deferred

class GameViewModel(
    private val productRepository: ProductRepository,
    private val highscoreRepository: HighscoreRepository,
    gameSettingProvider: GameSettingProvider
) : ViewModel() {

    private val TAG = this::class.java.simpleName

    private val shopifyProducts: Deferred<List<Product>> by lazyDeferred {
        productRepository.getProductsFromDatabase()
    }

    private var gameTimer: CountDownTimer? = null

    private val gameTimeInMs: Long = gameSettingProvider.getGameTimeInMs()
    val minPairsToFind: Int = gameSettingProvider.getMinPairsToFind()
    val numCardsToMatch: Int = gameSettingProvider.getNumCardsToMatch()

    var cardGuesses = MutableLiveData<MutableList<Card>>()
    var remainingTime = MutableLiveData<Long>()
    var score = MutableLiveData<Int>()
    var numCardsFound = MutableLiveData<Int>()
    var cardPairs = MutableLiveData<MutableList<Card>>()

    init {
        cardGuesses.value = mutableListOf()
        cardPairs.value = mutableListOf()
        score.value = 0
        numCardsFound.value = 0
    }

    /**
     * Init game
     */
    suspend fun createGame() {
        createCardPairs()
        startGameTimer(gameTimeInMs)
    }

    /**
     * Creates the cards that will be used in the game from the Shopify products
     *
     * 1. Randomly grab all products in the database
     * 2. Choose N cards for the minimum number of cards to find
     * 3. Duplicate M times for the number of cards to form a pair
     */
    private suspend fun createCardPairs() {
        val productList = shopifyProducts.await()

        Log.d(TAG, "Minimum card pairs to find: $minPairsToFind")
        Log.d(TAG, "Number of cards to match: $numCardsToMatch")

        if (!productList.isNullOrEmpty()) {
            for (card in 0 until minPairsToFind) {
                for (cardCopy in 0 until numCardsToMatch) {
                    cardPairs.value!!.add(Card(productList[card].id, productList[card].image.src))
                }
            }
        }

        cardPairs.value = cardPairs.value
    }

    /**
     * Add a card to the guess list
     *
     * @param card
     */
    fun addCardToGuess(card: Card) {
        cardGuesses.value?.add(card)
        cardGuesses.value = cardGuesses.value
    }

    /**
     * Removes all cards from the guess list
     *
     * @param card
     */
    fun removeAllCardsFromGuess() {
        cardGuesses.value?.clear()
        cardGuesses.value = cardGuesses.value
    }

    /**
     * Checks if all cards in the guess list are the same
     *
     * Select the first card, if it is the same as every other card in the list, all cards
     * are the same
     */
    fun doCardGuessesMatch(): Boolean {
        if (cardGuesses.value.isNullOrEmpty()) {
            return false
        }

        val cards = cardGuesses.value!!
        val cardToMatch = cards[0]

        val allGuessesMatch: Boolean = cards.all { card ->
            card.id == cardToMatch.id
        }

        Log.d(TAG, "Do all cards in the guess match: $allGuessesMatch")

        return allGuessesMatch
    }

    /**
     * Checks that all of the cards in the game have the card status FOUND.
     */
    fun areAllCardsFound(): Boolean {
        if (cardGuesses.value.isNullOrEmpty()) {
            return false
        }

        val cards = cardPairs.value!!

        val allCardsFound: Boolean = cards.all { card ->
            card.cardStatus == CardStatus.FOUND
        }

        Log.d(TAG, "Is game finished: $allCardsFound")

        return allCardsFound
    }

    /**
     * Shuffles the cards in the game.
     * This maintains each of the card's card status
     */
    fun shuffleCards() {
        val shuffledCards = cardPairs.value!!
        shuffledCards.shuffle()
        cardPairs.value = shuffledCards
    }

    /**
     * Score is created by number of time in seconds remaining
     */
    fun updateScore() {
        val seconds: Int = (remainingTime.value!! / 1000 % 60).toInt()
        score.value = score.value?.plus(seconds)
    }

    fun updateNumCardsFound() {
        numCardsFound.value = numCardsFound.value?.plus(1)
    }

    private fun startGameTimer(timeMs: Long) {
        gameTimer = object : CountDownTimer(timeMs, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTime.value = millisUntilFinished
            }

            override fun onFinish() {
                remainingTime.value = 0
            }

        }.start()
    }

    fun pauseGameTimer() {
        gameTimer?.cancel()
        gameTimer = null
    }

    fun saveScoreIntoDatabase() {
        highscoreRepository.saveHighscoreToDatabase(Highscore(score.value!!))
    }
}