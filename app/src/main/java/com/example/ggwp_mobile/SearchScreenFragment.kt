package com.example.ggwp_mobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import kotlinx.android.synthetic.main.fragment_search_screen_.*


class SearchScreenFragment : Fragment() {

    val key: String = "RGAPI-e5185b8b-d62f-436a-9df7-442951ef2200"
    val url: String = "https://eun1.api.riotgames.com/lol/summoner/v4/summoners/by-name/Davidoski?api_key=RGAPI-b11ef8a1-1f98-4163-a13b-e6d58f47e0f2"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val layout = inflater.inflate(R.layout.fragment_search_screen_, container, false)
        val editSummoner: EditText = layout.findViewById(R.id.editTextSummonerName)
        val textSummoner: TextView = layout.findViewById(R.id.textView)
        var apiResponse: String = "sd"
//        var apiResponse: String
//        thread {
//            apiResponse = URL("https://eun1.api.riotgames.com/lol/summoner/v4/summoners/by-name/Davidoski?api_key=RGAPI-d514c47e-e06f-4d8c-bd8a-503fef034e21").readText()
//            println(apiResponse)
//        }

//        GlobalScope.launch(Dispatchers.Main) {
//            apiResponse = URL("https://eun1.api.riotgames.com/lol/summoner/v4/summoners/by-name/Davidoski?api_key=RGAPI-d514c47e-e06f-4d8c-bd8a-503fef034e21").readText()
//            textSummoner.text = apiResponse
//            println(apiResponse)
//        //text
//        }
        //textSummoner.text = apiResponse
        /*thread {

            Thread.sleep(1000)
            val apiResponse = URL("https://eun1.api.riotgames.com/lol/summoner/v4/summoners/by-name/Davidoski?api_key=RGAPI-b11ef8a1-1f98-4163-a13b-e6d58f47e0f2").readText()
        }*/
        //val apiResponse = URL("https://eun1.api.riotgames.com/lol/summoner/v4/summoners/by-name/Davidoski?api_key=RGAPI-b11ef8a1-1f98-4163-a13b-e6d58f47e0f2").readText()
        //
//        sss.runOnUiThread(Runnable {
//            // change UI elements here
//        })

        CoroutineScope(IO).launch {
            fakeSummoner()
        }
        //println("hmmmmmmmmmmmmmmmmmmmmmmmmmmmm$apiResponse")

        return layout
    }

    private fun setNewText(input: String){
//        val textSummoner: TextView = layout.(R.id.textView)
//        text.textView = apiResponse
//        layoutInflater.textView
        println("\n$input")
        textView.text = "\n$input"
    }

    private suspend fun setTextOnMainThread(input: String){
        withContext(Main){
            setNewText(input)
        }
    }

    private suspend fun fakeSummoner(){
        val apiResponse = getSummoner()
        setTextOnMainThread(apiResponse)
    }

    private suspend fun getSummoner(): String {
        logThread("getSummoner")
        //val apiResponse = URL("https://eun1.api.riotgames.com/lol/summoner/v4/summoners/by-name/Davidoski?api_key=RGAPI-d514c47e-e06f-4d8c-bd8a-503fef034e21").readText()
        return URL("https://eun1.api.riotgames.com/lol/summoner/v4/summoners/by-name/Davidoski?api_key=RGAPI-d514c47e-e06f-4d8c-bd8a-503fef034e21").readText()
        //setTextOnMainThread(apiResponse)
    }

    private fun logThread(methodName: String) {
        //println("debug: ${methodName}: ${Thread.currentThread().name}")
    }
}
