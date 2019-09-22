package com.example.memorymatcher.ui.highscores


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.memorymatcher.R
import com.example.memorymatcher.data.db.entity.Highscore
import com.example.memorymatcher.util.Coroutines
import kotlinx.android.synthetic.main.fragment_high_scores.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

/**
 * A simple [Fragment] subclass.
 */
class HighscoresFragment : Fragment(), KodeinAware {

    override val kodein by closestKodein()
    private val highscoresViewModelFactory: HighscoresViewModelFactory by instance()

    private lateinit var highscoreViewModel: HighscoresViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        highscoreViewModel =
            ViewModelProvider(this, highscoresViewModelFactory).get(HighscoresViewModel::class.java)

        bindUI()

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_high_scores, container, false)
    }

    private fun bindUI() = Coroutines.main {
        initRecyclerView(highscoreViewModel.getHighscores())
    }

    private fun initRecyclerView(highscores: List<Highscore>) {
        highscoreRecyclerView.layoutManager = LinearLayoutManager(context)
        highscoreRecyclerView.adapter = HighscoreAdapter(highscores)
    }
}
