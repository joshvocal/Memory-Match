package com.example.memorymatcher.ui.game

import com.example.memorymatcher.data.db.entity.Image
import com.example.memorymatcher.data.db.entity.Product
import com.example.memorymatcher.data.provider.GameSettingProvider
import com.example.memorymatcher.data.repository.HighscoreRepository
import com.example.memorymatcher.data.repository.ProductRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GameViewModelTest {

    private lateinit var gameViewModel: GameViewModel

    private val products = listOf(
        Product(1, "Hello", Image(1, 1,1,1,"1")),
        Product(2, "Hello", Image(2, 2,2,2,"1"))
    )

    @Mock
    private lateinit var productRepository: ProductRepository

    @Mock
    private lateinit var highscoreRepository: HighscoreRepository

    @Mock
    private lateinit var gamePreferenceProvider: GameSettingProvider

    @Before
    fun setUp(){
        gameViewModel = GameViewModel(productRepository, highscoreRepository, gamePreferenceProvider)
    }

    @Test
    fun aTest() {

    }
}