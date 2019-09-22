package com.example.memorymatcher.ui.game


import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.memorymatcher.R
import com.example.memorymatcher.data.model.Card
import com.example.memorymatcher.data.model.CardStatus
import com.example.memorymatcher.util.Coroutines
import kotlinx.android.synthetic.main.fragment_game.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

/**
 * A simple [Fragment] subclass where you play the memory matching game.
 */
class GameFragment : Fragment(), KodeinAware {

    override val kodein by closestKodein()
    private val gameViewModelFactory: GameViewModelFactory by instance()

    private lateinit var gameViewModel: GameViewModel

    private lateinit var cardAdapter: CardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        gameViewModel =
            ViewModelProvider(this, gameViewModelFactory).get(GameViewModel::class.java)

        bindUI()

        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    /**
     * Binds the live-data
     */
    private fun bindUI() = Coroutines.main {
        observeCardPairs()

        observeRemainingGameTime()

        observeGameScore()

        observeNumberOfCardsFound()

        observeCardGuesses()

        gameViewModel.createGame()

        shuffleButton.setOnClickListener {
            gameViewModel.shuffleCards()
        }
    }

    /**
     * Updates the text for number of card pairs found when changed in the game bar
     */
    private fun observeCardPairs() {
        gameViewModel.cardPairs.observe(this, Observer { cardPairs ->
            if (cardPairs == null) {
                return@Observer
            }

            initRecyclerView(cardPairs)
        })
    }

    /**
     * Updates the text for remaining time in the game when changed in the game bar
     *
     * The game ends when there is no more remaining time
     */
    private fun observeRemainingGameTime() {
        gameViewModel.remainingTime.observe(this, Observer { milliseconds ->
            val seconds = milliseconds / 1000 % 60
            timeTextView.text = seconds.toString()

            if (milliseconds == 0L) {
                showEndGameDialog(false)
            }
        })
    }

    /**
     * Updates the text for total game score when changed in the game bar
     */
    private fun observeGameScore() {
        gameViewModel.score.observe(this, Observer { score ->
            scoreTextView.text = score.toString()
        })
    }

    /**
     * Updates the text for number of cards pairs found in the game bar
     */
    private fun observeNumberOfCardsFound() {
        gameViewModel.numCardsFound.observe(this, Observer { numCardsFound ->
            pairsTextView.text = getString(
                R.string.cards_found,
                numCardsFound.toString(),
                gameViewModel.minPairsToFind.toString()
            )
        })
    }

    /**
     * Updates the text for number of card guesses attempted in the game bar
     *
     * Limits the number of cards that you can click in the recycler view
     *
     * Ex. You set the game to have 2 guesses
     * Once you have added 2 cards to your guess list, the recycler view will be disabled
     * temporarily. There will be a check if all the cards are the same. The guess list will
     * than be emptied and clicks will be re-enabled
     */
    private fun observeCardGuesses() {
        gameViewModel.cardGuesses.observe(this, Observer { cardGuesses ->
            if (cardGuesses == null) {
                return@Observer
            }

            // Set the number of guesses remaining in the game tool bar
            // Ex. 0/2
            guessTextView.text = getString(
                R.string.cards_found,
                cardGuesses.size.toString(),
                gameViewModel.numCardsToMatch.toString()
            )

            if (cardGuesses.size == gameViewModel.numCardsToMatch) {
                cardAdapter.disableClicks()

                if (gameViewModel.doCardGuessesMatch()) {
                    Handler().postDelayed({
                        cardAdapter.setCardsFound()

                        gameViewModel.updateScore()
                        gameViewModel.updateNumCardsFound()

                        // End game if all cards are found
                        if (gameViewModel.areAllCardsFound()) {
                            gameViewModel.saveScoreIntoDatabase()
                            showEndGameDialog(true)
                            gameViewModel.pauseGameTimer()
                        }

                        gameViewModel.removeAllCardsFromGuess()
                        cardAdapter.enableClicks()
                    }, 1000)
                } else {
                    Handler().postDelayed({
                        cardAdapter.flipCardsDown()

                        gameViewModel.removeAllCardsFromGuess()
                        cardAdapter.enableClicks()
                    }, 1000)
                }

            }
        })
    }

    /**
     * Creates the recycler view for the products to match
     *
     * @param products List of cards to match
     */
    private fun initRecyclerView(products: List<Card>) {
        cardAdapter = CardAdapter(products, ::onCardClick)

        recyclerView.layoutManager = GridLayoutManager(context, 6)
        recyclerView.adapter = cardAdapter
    }

    /**
     * Callback function for the CardAdapter which allows use to add the card clicked into
     * the guess list.
     *
     * @param card Card that was clicked
     * @param cardPosition Position in the recycler view of the card clicked
     */
    private fun onCardClick(card: Card, cardPosition: Int) {
        when {
            card.cardStatus == CardStatus.DOWN -> {
                cardAdapter.flipCardUp(cardPosition)
                gameViewModel.addCardToGuess(card)
            }
            card.cardStatus == CardStatus.UP -> {
                // Do Nothing
            }
            card.cardStatus == CardStatus.FOUND -> {
                // Do Nothing
            }
        }
    }

    /**
     * Shows a dialog that is un-cancelable if you try to click outside of it.
     * This dialog brings you back to the main page.
     *
     * @param didWin If the game is won
     */
    private fun showEndGameDialog(didWin: Boolean) {
        val dialog = Dialog(activity!!)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(false)

        if (didWin) {
            dialog.setContentView(R.layout.dialog_finish_game)
        } else {
            dialog.setContentView(R.layout.dialog_lose_game)
        }

        val dialogButton = dialog.findViewById<Button>(R.id.playAgainButton)

        dialogButton.setOnClickListener {
            dialog.cancel()

            val action: NavDirections =
                GameFragmentDirections.actionGameFragmentToHomeFragment()
            NavHostFragment.findNavController(this).navigate(action)
        }

        dialog.show()
    }

    override fun onStart() {
        super.onStart()

        // Hide action bar for full game view
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.hide()
    }
}


