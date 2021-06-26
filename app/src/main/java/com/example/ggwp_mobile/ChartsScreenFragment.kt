package com.example.ggwp_mobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController


class ChartsScreenFragment: Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_charts_screen, container, false)

        val barChartButton = layout.findViewById<Button>(R.id.barChartButton)

        barChartButton.setOnClickListener { view ->
            //view.findNavController().navigate(R.id.action_chartsScreenFragment_to_chartScreenFragment)
            view.findNavController().navigate(R.id.chartScreenFragment)
        }

        return layout
    }
}