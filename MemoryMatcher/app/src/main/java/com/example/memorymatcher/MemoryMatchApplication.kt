package com.example.memorymatcher

import android.app.Application
import com.example.memorymatcher.data.db.HighScoreDatabase
import com.example.memorymatcher.data.db.ProductDatabase
import com.example.memorymatcher.data.network.service.ConnectivityInterceptor
import com.example.memorymatcher.data.network.service.ShopifyProductsService
import com.example.memorymatcher.data.provider.GameSettingProvider
import com.example.memorymatcher.data.repository.HighscoreRepository
import com.example.memorymatcher.data.repository.ProductRepository
import com.example.memorymatcher.ui.game.GameViewModelFactory
import com.example.memorymatcher.ui.highscores.HighscoresViewModelFactory
import com.facebook.stetho.Stetho
import org.kodein.di.Kodein.Companion.lazy
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class MemoryMatchApplication : Application(), KodeinAware {

    // Dependency Injection
    override val kodein = lazy {
        import(androidXModule(this@MemoryMatchApplication))

        bind() from singleton { ConnectivityInterceptor(instance()) }
        bind() from singleton { ShopifyProductsService(instance()) }

        bind() from singleton { ProductDatabase(instance()) }
        bind() from singleton { HighScoreDatabase(instance()) }

        bind() from singleton { ProductRepository(instance(), instance()) }
        bind() from singleton { HighscoreRepository(instance()) }

        bind() from singleton { GameSettingProvider(instance()) }

        bind() from provider { GameViewModelFactory(instance(), instance(), instance()) }
        bind() from provider { HighscoresViewModelFactory(instance()) }
    }

    override fun onCreate() {
        super.onCreate()

        // Use chrome://inspect/ in Chrome to view database on devivce
        Stetho.initializeWithDefaults(this)
    }
}