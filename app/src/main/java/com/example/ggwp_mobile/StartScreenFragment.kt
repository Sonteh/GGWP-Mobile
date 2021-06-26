package com.example.ggwp_mobile

import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_start_screen.*


class StartScreenFragment : Fragment() {

    private val viewModel: SummonerDataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_start_screen, container, false)

        val buttonStart: Button = layout.findViewById(R.id.buttonStart)

        buttonStart.setOnClickListener { view ->
            viewModel.updateSummonerName(editTextTextPersonName2!!.text.toString())
            view.findNavController().navigate(R.id.SearchScreenFragment)
            //
//            val kek = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
//            kek.isEnabled = false

            val lej = requireActivity().findViewById<CoordinatorLayout>(R.id.navbarLayout)
            lej.visibility = View.VISIBLE
        }
        return layout
    }
}