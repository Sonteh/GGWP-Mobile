package com.example.ggwp_mobile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController


class StartScreenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_start_screen, container, false)

        val buttonStart: Button = layout.findViewById(R.id.buttonStart)

        buttonStart.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_startScreenFragment_to_SearchScreenFragment)
        }

        return layout
    }
}