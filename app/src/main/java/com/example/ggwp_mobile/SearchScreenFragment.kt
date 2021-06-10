package com.example.ggwp_mobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import kotlinx.android.synthetic.main.fragment_search_screen.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL


class SearchScreenFragment : Fragment() {

    //place for changing api key
    private val key: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_search_screen, container, false)


        val buttonSearch = layout.findViewById<Button>(R.id.button)

        //setup a button listener to search for summoner
        buttonSearch.setOnClickListener {
            val summoner = editTextTextPersonName!!.text.toString()
            println(summoner)

            //Start IO (input/output) coroutine for network operations
            CoroutineScope(IO).launch {
                fakeSummoner(summoner)
            }
        }

        return layout
    }

    //this function sets UI textView
    private fun setNewText(input: String){
        textView.text = input
    }

    //this function sets UI textView2
    private fun setNewText2(input: String){
        textView2.text = input
    }

    //this function sets UI imageView
    private fun setNewImage(input: String){
        logThread("setNewImage")
        //using library Picasso: https://github.com/square/picasso
        //for some reason it can be used on Main thread
        Picasso.get().load("https://raw.communitydragon.org/latest/game/assets/ux/summonericons/profileicon$input.png").into(imageView)
    }

    //calls set functions with parameters
    private suspend fun setTextOnMainThread(input: String, input2: String, input3: String){ //suspend marks this function as something that can be asynchronous
        //start Main coroutine for operations on UI
        withContext(Main){
            setNewText(input)
            setNewText2("Global mastery score: $input2")
            setNewImage(input3)
        }
    }

    private suspend fun fakeSummoner(input: String): String{
        val summonerInfo = getSummoner(input) //calls

        //this parse string to json format
        val json = JSONObject(summonerInfo) //string instance holding the above json
        val summonerId = json.getString("id") //get value by key
        val summonerIcon = json.getString("profileIconId") //get value by key

        val masteryScore = getMasteryScore(summonerId)

        setTextOnMainThread(summonerInfo, masteryScore, summonerIcon)

        return summonerId //testing here, THIS DOES NOTHING FOR NOW
    }

    //gets summoner ids by summoner name (api request: summoner by name)
    private fun getSummoner(input: String): String {
        return URL("https://eun1.api.riotgames.com/lol/summoner/v4/summoners/by-name/$input?api_key=$key").readText()
    }

    //gets global mastery score by summonerId (api request: mastery by summonerId)
    private fun getMasteryScore(input: String): String {
        return URL("https://eun1.api.riotgames.com/lol/champion-mastery/v4/scores/by-summoner/$input?api_key=$key").readText()
    }

    //function to log current thread where operation is taking place
    //can be deleted later
    private fun logThread(methodName: String) {
        println("Thread debug: ${methodName}: ${Thread.currentThread().name}")
    }
}
