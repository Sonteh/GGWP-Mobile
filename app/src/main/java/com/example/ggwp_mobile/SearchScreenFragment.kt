package com.example.ggwp_mobile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_search_screen.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.util.*


class SearchScreenFragment : Fragment() {

    private val viewModel: SummonerDataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_search_screen, container, false)

        val key = viewModel.returnKey()

        val summoner = viewModel.returnSummonerName() //editTextTextPersonName!!.text.toString() Moved it to starting screen
        val summonerRegionCode = viewModel.returnSummonerRegionCode()
        println(summoner)

        //Start IO (input/output) coroutine for network operations
        CoroutineScope(IO).launch {
            fakeSummoner(key, summonerRegionCode)
        }
        return layout
    }

    //this function sets UI totalMasteryView
    @SuppressLint("SetTextI18n")
    private fun setMasteryLvl(masteryLvl: String){
        totalMasteryView.text = masteryLvl
    }

    //this function sets UI nickView
    private fun setNickname(summonerName: String){
        nickView.text = summonerName
    }

    //this function sets UI accountLvlView
    @SuppressLint("SetTextI18n")
    private fun setAccountLvl(lvl: String){
        accountLvlView.text = "Level: $lvl"
    }

    @SuppressLint("SetTextI18n")
    private fun setRankedStats(rank: String, tier: String, lp: String, wins: Int, losses: Int, winRate: Double){
        rankView.text = "$rank $tier"
        lpView.text = "Lp: $lp"
        winsView.text = "Ranked W:\n$wins"
        lossesView.text = "Ranked L:\n$losses"
        winRateView.text = "Winrate: $winRate%"
    }

    //this function sets UI profileIconView
    private fun setNewImage(iconId: String, soloRank: String){
        logThread("setNewImage")
        //using library Picasso: https://github.com/square/picasso
        //for some reason it can be used on Main thread
        Picasso.get().load("https://raw.communitydragon.org/latest/game/assets/ux/summonericons/profileicon$iconId.png").into(profileIconView)

        var rank = soloRank.toLowerCase(Locale.ROOT)
        rank = rank.capitalize(Locale.ROOT)
        Picasso.get().load("https://raw.githubusercontent.com/LightshieldDotDev/Chime-frontend/master/src/assets/emblems/"+rank+"_Emblem.png").into(soloRankImageView);
    }

    //calls set functions with parameters
    private suspend fun setTextOnMainThread(masteryScore: String, summonerIcon: String, summonerLvl: String, rank: String){ //suspend marks this function as something that can be asynchronous
        //start Main coroutine for operations on UI
        withContext(Main){
            setMasteryLvl("Mastery score: $masteryScore")
            setNewImage(summonerIcon, rank)
            setNickname(viewModel.returnSummonerName())
            setAccountLvl(summonerLvl)
        }
    }

    private suspend fun fakeSummoner(apiKey: String, summonerRegionCode: String): String
    {
        val summonerId = viewModel.returnSummonerId()
        val summonerIcon = viewModel.returnSummonerIcon()
        val summonerLvl = viewModel.returnSummonerLevel()

        val masteryScore = getMasteryScore(summonerId, apiKey, summonerRegionCode)

        val leagueEntries = getLeagueEntries(summonerId, apiKey, summonerRegionCode)
        val leagueEntriesJson = JSONArray(leagueEntries)
        val leagueEntriesJsonObject = leagueEntriesJson.getJSONObject(0)
        val tier = leagueEntriesJsonObject.getString("tier")

        setTextOnMainThread(masteryScore, summonerIcon, summonerLvl, tier)

        val rank = leagueEntriesJsonObject.getString("rank")
        val leaguePoints = leagueEntriesJsonObject.getString("leaguePoints")
        val wins = leagueEntriesJsonObject.getString("wins").toInt()
        val losses = leagueEntriesJsonObject.getString("losses").toInt()
        var winRate = wins.toDouble()/(wins.toDouble() + losses.toDouble())
        winRate = Math.round(winRate * 1000.0) / 10.0
        setRankedStats(tier, rank, leaguePoints, wins, losses, winRate)

        return summonerId //testing here, THIS DOES NOTHING FOR NOW
    }

    //gets global mastery score by summonerId (api request: mastery by summonerId)
    private fun getMasteryScore(summonerId: String, apiKey: String, summonerRegionCode: String): String {
        return URL("https://$summonerRegionCode.api.riotgames.com/lol/champion-mastery/v4/scores/by-summoner/$summonerId?api_key=$apiKey").readText()
    }

    //gets league entries by summonerId (api request: league entries by summoner Id)
    private fun getLeagueEntries(summonerId: String, apiKey: String, summonerRegionCode: String): String {
        return URL("https://$summonerRegionCode.api.riotgames.com/lol/league/v4/entries/by-summoner/$summonerId?api_key=$apiKey").readText()
    }

    //function to log current thread where operation is taking place
    //can be deleted later
    private fun logThread(methodName: String) {
        println("Thread debug: ${methodName}: ${Thread.currentThread().name}")
    }


}
