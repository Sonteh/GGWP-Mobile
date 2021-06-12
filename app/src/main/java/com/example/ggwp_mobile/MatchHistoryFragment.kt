package com.example.ggwp_mobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import kotlinx.android.synthetic.main.fragment_match_history_screen.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL


class MatchHistoryFragment : Fragment() {

    private val viewModel: SummonerDataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val layout = inflater.inflate(R.layout.fragment_match_history_screen, container, false)

        val puuid = viewModel.returnpuuId()
        val key = viewModel.returnKey()
        CoroutineScope(Dispatchers.IO).launch {
            matchHistory(puuid, key)
        }

        return layout
    }

    private fun setNewText3(input: String) {
        textView3.text = input
    }

    private fun setTextOnMainThread(input: String){
        CoroutineScope(Dispatchers.Main).launch {
            setNewText3(input)
        }
    }

    private fun matchHistory(input: String, input2: String) {
        val matchIds = getMatchIds(input, input2)

        setTextOnMainThread(matchIds)
    }

    private fun getMatchIds(input: String, input2: String): String {
        return URL("https://europe.api.riotgames.com/lol/match/v5/matches/by-puuid/$input/ids?start=0&count=20&api_key=$input2").readText()
    }
}