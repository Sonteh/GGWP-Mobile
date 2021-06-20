package com.example.ggwp_mobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.beust.klaxon.Json
import com.beust.klaxon.JsonObject
import kotlinx.android.synthetic.main.fragment_match_history_screen.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
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
            val matchHistory = getMatchHistory(puuid, key)

            val matchDetails = getMatchDetails(key, matchHistory.getString(0))

            val json = JSONObject(matchDetails)
            val gameInfo = json.getString("info")

            println(gameInfo)

            println(matchDetails)


        }

        return layout
    }

//    private fun setTextOnMainThread(input: String)
//    {
//        CoroutineScope(Dispatchers.Main).launch {
//            setNewText3(input)
//    }

    private fun getMatchHistory(input: String, input2: String): JSONArray
    {
        val matchIds = getMatchIds(input, input2)

        val json = JSONArray(matchIds)

        return json
    }

    private fun getMatchIds(apiKey: String, puuid: String): String {
        return URL("https://europe.api.riotgames.com/lol/match/v5/matches/by-puuid/$puuid/ids?start=0&count=20&api_key=$apiKey").readText()
    }

    private fun getMatchDetails(apiKey: String, matchId: String): String
    {
        return URL("https://europe.api.riotgames.com/lol/match/v5/matches/$matchId?api_key=$apiKey").readText()
    }
}