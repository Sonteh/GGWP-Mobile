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
import org.json.JSONObject
import java.net.URL


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

    //this function sets UI nickView
    private fun setAccountLvl(input: String){
        accountLvlView.text = input
    }

    //this function sets UI profileIconView
    private fun setNewImage(input: String){
        logThread("setNewImage")
        //using library Picasso: https://github.com/square/picasso
        //for some reason it can be used on Main thread
        Picasso.get().load("https://raw.communitydragon.org/latest/game/assets/ux/summonericons/profileicon$input.png").into(profileIconView)
    }

    //calls set functions with parameters
    private suspend fun setTextOnMainThread(input: String, input2: String, input3: String, input4: String){ //suspend marks this function as something that can be asynchronous
        //start Main coroutine for operations on UI
        withContext(Main){
            setNewText(input)
            setMasteryLvl("Global mastery score: $input2")
            setNewImage(input3)
            setNickname(viewModel.returnSummonerName())
            setAccountLvl(input4)
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

        setTextOnMainThread(summonerInfo, masteryScore, summonerIcon, summonerLvl)

        return summonerId //testing here, THIS DOES NOTHING FOR NOW
    }

    //gets summoner ids by summoner name (api request: summoner by name)
    private fun getSummoner(input: String, input2: String): String {
        return URL("https://eun1.api.riotgames.com/lol/summoner/v4/summoners/by-name/$input?api_key=$input2").readText()
    }

    //gets global mastery score by summonerId (api request: mastery by summonerId)
    private fun getMasteryScore(input: String, input2: String): String {
        return URL("https://eun1.api.riotgames.com/lol/champion-mastery/v4/scores/by-summoner/$input?api_key=$input2").readText()
    }

    //function to log current thread where operation is taking place
    //can be deleted later
    private fun logThread(methodName: String) {
        println("Thread debug: ${methodName}: ${Thread.currentThread().name}")
    }


}
