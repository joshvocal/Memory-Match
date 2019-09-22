package com.example.memorymatcher.data.provider

import android.content.Context

class GameSettingProvider(context: Context) : PreferenceProvider(context) {

    fun getNumCardsToMatch(): Int {
        return preferences.getString("NUM_CARDS_TO_MATCH", "2")!!.toInt()
    }

    fun getMinPairsToFind(): Int {
        return preferences.getString("MIN_PAIRS_TO_FIND", "10")!!.toInt()
    }

    fun getGameTimeInMs(): Long {
        return preferences.getString("TIME", "60000")!!.toLong()
    }
}