package com.example.ggwp_mobile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL


class MatchHistoryFragment : Fragment() {

    private val viewModel: SummonerDataViewModel by activityViewModels()

    @SuppressLint("SetTextI18n", "InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val layout = inflater.inflate(R.layout.fragment_match_history_screen, container, false)

        val puuid = viewModel.returnpuuId()
        val key = viewModel.returnKey()
        val summonerName2 = viewModel.returnSummonerName()

        val linearLayout: LinearLayout = layout.findViewById(R.id.layout_main)

        CoroutineScope(Dispatchers.IO).launch {
            val matchHistory = getMatchHistory(key, puuid)

            for (i in 0..19)
            {
                val matchDetails = getMatchDetails(key, matchHistory.getString(i))
                val json = JSONObject(matchDetails)
                //val gameInfo = json.getString("info")
                val gameInfo = json.getJSONObject("info")

                val participants = gameInfo.getJSONArray("participants")

                for(j in 0..9)
                {
                    val participant = participants.getJSONObject(j)
                    val summonerName = participant.get("summonerName")

                    if (summonerName == summonerName2)
                    {
                        val champion = participant.get("championName")
                        withContext(Main) {
                            val view: View = layoutInflater.inflate(R.layout.match_child, null)
                            val matchItem: TextView = view.findViewById(R.id.match_item)
                            matchItem.text = "$summonerName played $champion"
                            linearLayout.addView(view)
                        }
                        println("$summonerName played $champion")
                    }
                }

                //println("$participants")

            }

        }

        return layout
    }

//    private fun setTextOnMainThread(input: String)
//    {
//        CoroutineScope(Dispatchers.Main).launch {
//            setNewText3(input)
//    }

    private fun getMatchHistory(apiKey: String, puuid: String): JSONArray
    {
        val matchIds = getMatchIds(apiKey, puuid)

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