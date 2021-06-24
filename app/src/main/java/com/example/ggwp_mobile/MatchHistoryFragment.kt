package com.example.ggwp_mobile

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.ui.text.toLowerCase
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_match_history_screen.*
import kotlinx.android.synthetic.main.match_child.*
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
                        val championName = participant.get("championName")
                        val championId = participant.get("championId")
                        val kills = participant.get("kills")
                        val deaths = participant.get("deaths")
                        val assists = participant.get("assists")

                        withContext(Main) {
                            val view: View = layoutInflater.inflate(R.layout.match_child, null)
                            val matchItem: TextView = view.findViewById(R.id.match_item)

                            Picasso.get().load("https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1/champion-icons/$championId.png").into(object: com.squareup.picasso.Target {

                                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?)
                                {
                                    //Log.v("DEBUG", "onBitmapLoaded")
                                    val drawImage: Drawable = BitmapDrawable(resources, bitmap)
                                    matchItem.setCompoundDrawablesWithIntrinsicBounds(drawImage, null, null, null)
                                }

                                override fun onPrepareLoad(placeHolderDrawable: Drawable?)
                                {}

                                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?)
                                {}
                            })
                            matchItem.text = "$summonerName played $championName $kills/$deaths/$assists"
                            linearLayout.addView(view)
                        }
                        println("$summonerName played $championName")
                    }
                }
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