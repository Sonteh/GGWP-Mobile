package com.example.ggwp_mobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
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

        val buttonGoToMatchHistory = layout.findViewById<Button>(R.id.matchHistoryButton)

        val summoner = viewModel.returnSummonerName() //editTextTextPersonName!!.text.toString() Moved it to starting screen
        println(summoner)

        //Start IO (input/output) coroutine for network operations
        CoroutineScope(IO).launch {
            fakeSummoner(summoner, key)
        }

        buttonGoToMatchHistory.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_SearchScreenFragment_to_matchHistoryFragment)
        }

        return layout
    }

    //this function sets UI textView
    private fun setNewText(input: String){
        textView.text = input
    }

    //this function sets UI totalMasteryView
    private fun setMasteryLvl(input: String){
        totalMasteryView.text = input
    }

    //this function sets UI nickView
    private fun setNickname(input: String){
        nickView.text = input
    }

    //this function sets UI accountLvlView
    private fun setAccountLvl(input: String){
        accountLvlView.text = input
    }

    //this function sets UI leagueEntriesView
    private fun setLeagueEntries(input: String){
        leagueEntriesView.text = input
    }

    //this function sets UI profileIconView
    private fun setNewImage(input: String, input2: String){
        logThread("setNewImage")
        //using library Picasso: https://github.com/square/picasso
        //for some reason it can be used on Main thread
        Picasso.get().load("https://raw.communitydragon.org/latest/game/assets/ux/summonericons/profileicon$input.png").into(profileIconView)

        var rank = input2.toLowerCase(Locale.ROOT)
        rank = rank.capitalize(Locale.ROOT)
        Picasso.get().load("https://raw.githubusercontent.com/LightshieldDotDev/Chime-frontend/master/src/assets/emblems/"+rank+"_Emblem.png").into(soloRankImageView);
    }

    //calls set functions with parameters
    private suspend fun setTextOnMainThread(summonerInfo: String, masteryScore: String, summonerIcon: String, summonerLvl: String, leagueEntries: String, rank: String){ //suspend marks this function as something that can be asynchronous
        //start Main coroutine for operations on UI
        withContext(Main){
            setNewText(summonerInfo)
            setMasteryLvl("Global mastery score: $masteryScore")
            setNewImage(summonerIcon, rank)
            setNickname(viewModel.returnSummonerName())
            setAccountLvl(summonerLvl)
            setLeagueEntries(leagueEntries)
        }
    }

    private suspend fun fakeSummoner(input: String, input2: String): String{
        val summonerInfo = getSummoner(input, input2)

        //this parse string to json format
        val json = JSONObject(summonerInfo) //string instance holding the above json
        val summonerId = json.getString("id") //get value by key
        val summonerIcon = json.getString("profileIconId") //get value by key
        val summonerLvl = json.getString("summonerLevel") //get value by key



        val masteryScore = getMasteryScore(summonerId, input2)

        viewModel.updatepuuId(json.getString("puuid"))


        val leagueEntries = getLeagueEntries(summonerId, input2)
        val json2 = JSONArray(leagueEntries)
        val rank = json2.getJSONObject(0)
        val tier = rank.getString("tier")
        setTextOnMainThread(summonerInfo, masteryScore, summonerIcon, summonerLvl, leagueEntries, tier)

        return summonerId //testing here, THIS DOES NOTHING FOR NOW
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
