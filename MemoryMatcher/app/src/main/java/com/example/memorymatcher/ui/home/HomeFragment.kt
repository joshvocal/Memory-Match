package com.example.memorymatcher.ui.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.example.memorymatcher.R
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonSettings.setOnClickListener {
            val action: NavDirections =
                HomeFragmentDirections.actionHomeFragmentToSettingsFragment()
            Navigation.findNavController(it).navigate(action)
        }

        buttonPlayGame.setOnClickListener {
            val action: NavDirections =
                HomeFragmentDirections.actionHomeFragmentToGameFragment()
            Navigation.findNavController(it).navigate(action)
        }

        buttonHighScores.setOnClickListener {
            val action: NavDirections =
                HomeFragmentDirections.actionHomeFragmentToHighScoresFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).supportActionBar?.show()
    }
}
