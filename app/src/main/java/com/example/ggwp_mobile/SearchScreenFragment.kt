package com.example.ggwp_mobile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_search_screen.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.URL
import java.util.*
import kotlin.math.roundToInt


class SearchScreenFragment : Fragment() {

    private val viewModel: SummonerDataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_search_screen, container, false)

        val key = viewModel.returnKey()
        val summonerRegionCode = viewModel.returnSummonerRegionCode()

        //Start IO (input/output) coroutine for network operations
        CoroutineScope(IO).launch {
            fakeSummoner(key, summonerRegionCode)
        }

        val soloQ = layout.findViewById<Button>(R.id.soloQButton)
        val flexQ = layout.findViewById<Button>(R.id.flexQButton)

        val soloQLayout = layout.findViewById<ConstraintLayout>(R.id.soloQLayout)
        val flexQLayout = layout.findViewById<ConstraintLayout>(R.id.flexQLayout)
        soloQ.setOnClickListener{
            soloQLayout.visibility = GONE
            flexQLayout.visibility = VISIBLE
            soloQ.visibility = GONE
            flexQ.visibility = VISIBLE
        }
        flexQLayout.visibility = GONE
        flexQ.visibility = GONE
        flexQ.setOnClickListener{
            flexQLayout.visibility = GONE
            soloQLayout.visibility = VISIBLE
            flexQ.visibility = GONE
            soloQ.visibility = VISIBLE
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
    private fun setRankedStats(rank: String, tier: String, lp: String, wins: Int, losses: Int, winRate: Double,
                               flexRank: String, flexTier: String, flexLeaguePoints: String, flexWins: Int, flexLosses: Int, flexWinRate: Double){
        rankView.text = "$rank $tier"
        lpView.text = "Lp: $lp"
        winsView.text = "Ranked W:\n$wins"
        lossesView.text = "Ranked L:\n$losses"
        winRateView.text = "Winrate: $winRate%"

        flexRankView.text = "$flexTier $flexRank"
        flexLpView.text = "Lp: $flexLeaguePoints"
        flexWinsView.text = "Ranked W:\n$flexWins"
        flexLossesView.text = "Ranked L:\n$flexLosses"
        flexWinRateView.text = "Winrate: $flexWinRate%"
    }

    //this function sets UI profileIconView
    private fun setNewImage(iconId: String, soloRank: String, flexRank: String){
        //using library Picasso: https://github.com/square/picasso
        //for some reason it can be used on Main thread
        Picasso.get().load("https://raw.communitydragon.org/latest/game/assets/ux/summonericons/profileicon$iconId.png").into(profileIconView)

        var rank = soloRank.toLowerCase(Locale.ROOT)
        rank = rank.capitalize(Locale.ROOT)
        Picasso.get().load("https://raw.githubusercontent.com/LightshieldDotDev/Chime-frontend/master/src/assets/emblems/"+rank+"_Emblem.png").into(soloRankImageView)

        var rank2 = flexRank.toLowerCase(Locale.ROOT)
        rank2 = rank2.capitalize(Locale.ROOT)
        Picasso.get().load("https://raw.githubusercontent.com/LightshieldDotDev/Chime-frontend/master/src/assets/emblems/"+rank2+"_Emblem.png").into(flexRankImageView)
        println("https://raw.githubusercontent.com/LightshieldDotDev/Chime-frontend/master/src/assets/emblems/"+rank+"_Emblem.png")
        println("https://raw.githubusercontent.com/LightshieldDotDev/Chime-frontend/master/src/assets/emblems/"+rank2+"_Emblem.png")
    }

    //calls set functions with parameters
    private suspend fun setTextOnMainThread(masteryScore: String, summonerIcon: String, summonerLvl: String, rank: String, flexRank: String){ //suspend marks this function as something that can be asynchronous
        //start Main coroutine for operations on UI
        withContext(Main){
            setMasteryLvl("Mastery: $masteryScore")
            setNewImage(summonerIcon, rank, flexRank)
            setNickname(viewModel.returnSummonerName())
            setAccountLvl(summonerLvl)
        }
    }

    private suspend fun fakeSummoner(apiKey: String, summonerRegionCode: String)
    {
        val summonerId = viewModel.returnSummonerId()
        val summonerIcon = viewModel.returnSummonerIcon()
        val summonerLvl = viewModel.returnSummonerLevel()

        val masteryScore = getMasteryScore(summonerId, apiKey, summonerRegionCode)

        val leagueEntries = getLeagueEntries(summonerId, apiKey, summonerRegionCode)
        val leagueEntriesJson = JSONArray(leagueEntries)

        var rank = "0"
        var tier = "Unranked"
        var leaguePoints = "0"
        var wins = 0
        var losses = 0
        var winRate = 0.00

        var flexRank = "0"
        var flexTier = "Unranked"
        var flexLeaguePoints = "0"
        var flexWins = 0
        var flexLosses = 0
        var flexWinRate = 0.00

        for (i in 0 until leagueEntriesJson.length()){
            val leagueInfo = leagueEntriesJson.getJSONObject(i)

            if (leagueInfo.getString("queueType") == "RANKED_SOLO_5x5") {
                rank = leagueInfo.getString("rank")
                tier = leagueInfo.getString("tier")
                leaguePoints = leagueInfo.getString("leaguePoints")
                wins = leagueInfo.getString("wins").toInt()
                losses = leagueInfo.getString("losses").toInt()
                winRate = wins.toDouble() / (wins.toDouble() + losses.toDouble())
                winRate = (winRate * 1000.0).roundToInt() / 10.0
            }
            if (leagueInfo.getString("queueType") == "RANKED_FLEX_SR") {
                flexRank = leagueInfo.getString("rank")
                flexTier = leagueInfo.getString("tier")
                flexLeaguePoints = leagueInfo.getString("leaguePoints")
                flexWins = leagueInfo.getString("wins").toInt()
                flexLosses = leagueInfo.getString("losses").toInt()
                flexWinRate = flexWins.toDouble()/(flexWins.toDouble() + flexLosses.toDouble())
                flexWinRate = (flexWinRate * 1000.0).roundToInt() / 10.0
            }
        }

        setTextOnMainThread(masteryScore, summonerIcon, summonerLvl, tier, flexTier)
        setRankedStats(tier, rank, leaguePoints, wins, losses, winRate, flexRank, flexTier, flexLeaguePoints, flexWins, flexLosses, flexWinRate)
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
    private fun logThread() {
        println("Thread debug: ${Thread.currentThread().name}")
    }
}
