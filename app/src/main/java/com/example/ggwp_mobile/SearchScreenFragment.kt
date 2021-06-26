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
import org.json.JSONObject
import java.io.FileNotFoundException
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
        println(summoner)

        //Start IO (input/output) coroutine for network operations
        CoroutineScope(IO).launch {
            fakeSummoner(summoner, key)
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

    //this function sets UI textView
    private fun setNewText(summonerData: String){
        //textView.text = summonerData
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

        flexRankView.text = "$flexRank $flexTier"
        flexLpView.text = "Lp: $flexLeaguePoints"
        flexWinsView.text = "Ranked W:\n$flexWins"
        flexLossesView.text = "Ranked L:\n$flexLosses"
        flexWinRateView.text = "Winrate: $flexWinRate%"
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
    private suspend fun setTextOnMainThread(summonerInfo: String, masteryScore: String, summonerIcon: String, summonerLvl: String, rank: String){ //suspend marks this function as something that can be asynchronous
        //start Main coroutine for operations on UI
        withContext(Main){
            setNewText(summonerInfo)
            setMasteryLvl("Mastery score: $masteryScore")
            setNewImage(summonerIcon, rank)
            setNickname(viewModel.returnSummonerName())
            setAccountLvl(summonerLvl)
        }
    }

    private suspend fun fakeSummoner(summonerName: String, apiKey: String): Unit{
        val summonerInfo = getSummoner(summonerName, apiKey)

        //this parse string to json format
        val json = JSONObject(summonerInfo) //string instance holding the above json
        val summonerId = json.getString("id") //get value by key
        val summonerIcon = json.getString("profileIconId") //get value by key
        val summonerLvl = json.getString("summonerLevel") //get value by key



        val masteryScore = getMasteryScore(summonerId, apiKey)

        viewModel.updatepuuId(json.getString("puuid"))
        viewModel.updateSummonerID(json.getString("id"))

        val leagueEntries = getLeagueEntries(summonerId, apiKey)
        val leagueEntriesJson = JSONArray(leagueEntries)
        var leagueEntriesJsonObject = leagueEntriesJson.getJSONObject(0)
        val tier = leagueEntriesJsonObject.getString("tier")
        setTextOnMainThread(summonerInfo, masteryScore, summonerIcon, summonerLvl, tier)

        var rank = leagueEntriesJsonObject.getString("rank")
        var leaguePoints = leagueEntriesJsonObject.getString("leaguePoints")
        var wins = leagueEntriesJsonObject.getString("wins").toInt()
        var losses = leagueEntriesJsonObject.getString("losses").toInt()
        var winRate = wins.toDouble() / (wins.toDouble() + losses.toDouble())
        winRate = Math.round(winRate * 1000.0) / 10.0


        var flexRank = "Unranked"
        var flexTier = "0"
        var flexLeaguePoints = "0"
        var flexWins = 0
        var flexLosses = 0
        var flexWinRate = 0.00


        for (i in 0 until leagueEntriesJson.length()){
            val test = leagueEntriesJson.getJSONObject(i)
            println(test)
            if (test.getString("queueType") == "RANKED_SOLO_5x5") {
                rank = test.getString("rank")
                leaguePoints = test.getString("leaguePoints")
                wins = test.getString("wins").toInt()
                losses = test.getString("losses").toInt()
                winRate = wins.toDouble() / (wins.toDouble() + losses.toDouble())
                winRate = Math.round(winRate * 1000.0) / 10.0
            }
            if (test.getString("queueType") == "RANKED_FLEX_SR") {
                flexRank = test.getString("rank")
                flexTier = test.getString("tier")
                flexLeaguePoints = test.getString("leaguePoints")
                flexWins = test.getString("wins").toInt()
                flexLosses = test.getString("losses").toInt()
                flexWinRate = flexWins.toDouble()/(flexWins.toDouble() + flexLosses.toDouble())
                flexWinRate = Math.round(flexWinRate * 1000.0) / 10.0
            }
        }


        setRankedStats(tier, rank, leaguePoints, wins, losses, winRate, flexRank, flexTier, flexLeaguePoints, flexWins, flexLosses, flexWinRate)

        //return summonerId //testing here, THIS DOES NOTHING FOR NOW
    }

    //gets summoner ids by summoner name (api request: summoner by name)
    private fun getSummoner(summonerName: String, apiKey: String): String {
        return URL("https://eun1.api.riotgames.com/lol/summoner/v4/summoners/by-name/$summonerName?api_key=$apiKey").readText()
    }

    //gets global mastery score by summonerId (api request: mastery by summonerId)
    private fun getMasteryScore(summonerId: String, apiKey: String): String {
        return URL("https://eun1.api.riotgames.com/lol/champion-mastery/v4/scores/by-summoner/$summonerId?api_key=$apiKey").readText()
    }

    //gets league entries by summonerId (api request: league entries by summoner Id)
    private fun getLeagueEntries(summonerId: String, apiKey: String): String {
        return URL("https://eun1.api.riotgames.com/lol/league/v4/entries/by-summoner/$summonerId?api_key=$apiKey").readText()
    }

    //function to log current thread where operation is taking place
    //can be deleted later
    private fun logThread(methodName: String) {
        println("Thread debug: ${methodName}: ${Thread.currentThread().name}")
    }


}
