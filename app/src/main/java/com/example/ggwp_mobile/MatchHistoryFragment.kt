package com.example.ggwp_mobile

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.squareup.picasso.Picasso
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

//        val cardView = layout.findViewById<CardView>(R.id.base_cardview)
//        val arrow = layout.findViewById<Button>(R.id.arrow_button)
//        val hiddenView = layout.findViewById<View>(R.id.hidden_view)

        val linearLayout: LinearLayout = layout.findViewById(R.id.layout_main)

        CoroutineScope(Dispatchers.IO).launch {
            val matchHistory = getMatchHistory(key, puuid)

            for (i in 0..19) {
                val matchDetails = getMatchDetails(key, matchHistory.getString(i))
                val json = JSONObject(matchDetails)
                //val gameInfo = json.getString("info")
                val gameInfo = json.getJSONObject("info")

                val participants = gameInfo.getJSONArray("participants")

                for (j in 0..9) {
                    val participant = participants.getJSONObject(j)
                    val summonerName = participant.get("summonerName")

                    if (summonerName == summonerName2) {
                        val championName = participant.get("championName")
                        val championId = participant.get("championId")
                        val kills = participant.get("kills")
                        val deaths = participant.get("deaths")
                        val assists = participant.get("assists")
                        val playerWin = participant.get("win").toString().toBoolean()
                        var playerResult: String = ""

                        withContext(Main) {
                            val view: View = layoutInflater.inflate(R.layout.match_child, null)
                            val matchItem: TextView = view.findViewById(R.id.match_item)
                            val cardView: CardView = view.findViewById(R.id.base_cardview)
                            val arrow: ImageButton = view.findViewById(R.id.arrow_button)
                            val hiddenView: View = view.findViewById(R.id.hidden_view)


                            Picasso.get()
                                .load("https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1/champion-icons/$championId.png")
                                .into(object : com.squareup.picasso.Target {

                                    override fun onBitmapLoaded(
                                        bitmap: Bitmap?,
                                        from: Picasso.LoadedFrom?
                                    ) {
                                        val drawImage: Drawable = BitmapDrawable(resources, bitmap)
                                        matchItem.setCompoundDrawablesWithIntrinsicBounds(
                                            drawImage,
                                            null,
                                            null,
                                            null
                                        )
                                    }

                                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

                                    override fun onBitmapFailed(
                                        e: Exception?,
                                        errorDrawable: Drawable?
                                    ) {
                                    }
                                })
                            if (playerWin) {
                                playerResult = "WIN"
                                matchItem.setBackgroundColor(Color.parseColor("#446cff"))
                            } else {
                                playerResult = "LOST"
                                matchItem.setBackgroundColor(Color.parseColor("#ff8385"))
                            }
                            matchItem.text = "$summonerName played $championName $kills/$deaths/$assists \n $playerResult"
                            arrow.setOnClickListener(getOnClick(cardView, hiddenView))
                            linearLayout.addView(view)
                        }
                        println("$summonerName played $championName")
                    }
                }
            }
        }
        return layout
    }

    private fun getOnClick(cardView: CardView, hiddenView: View): View.OnClickListener
    {
        return View.OnClickListener {
            println("TEST")
            // If the CardView is already expanded, set its visibility
            //  to gone and change the expand less icon to expand more.
            if (hiddenView.visibility == View.VISIBLE) {

                // The transition of the hiddenView is carried out
                //  by the TransitionManager class.
                // Here we use an object of the AutoTransition
                // Class to create a default transition.
                TransitionManager.beginDelayedTransition(
                    cardView,
                    AutoTransition()
                );
                hiddenView.visibility = View.GONE;
                // arrow.setImageResource(R.drawable.ic_baseline_expand_more_24);
            }

            // If the CardView is not expanded, set its visibility
            // to visible and change the expand more icon to expand less.
            else {

                TransitionManager.beginDelayedTransition(
                    cardView,
                    AutoTransition()
                );
                hiddenView.visibility = View.VISIBLE;
                // arrow.setImageResource(R.drawable.ic_baseline_expand_less_24);
            }
        }
    }

    private fun getMatchHistory(apiKey: String, puuid: String): JSONArray {
        val matchIds = getMatchIds(apiKey, puuid)

        return JSONArray(matchIds)
    }

    private fun getMatchIds(apiKey: String, puuid: String): String {
        return URL("https://europe.api.riotgames.com/lol/match/v5/matches/by-puuid/$puuid/ids?start=0&count=20&api_key=$apiKey").readText()
    }

    private fun getMatchDetails(apiKey: String, matchId: String): String
    {
        return URL("https://europe.api.riotgames.com/lol/match/v5/matches/$matchId?api_key=$apiKey").readText()
    }
}