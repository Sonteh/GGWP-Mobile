package com.example.ggwp_mobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_search_screen.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL


class SearchScreenFragment : Fragment() {

    val key: String = "RGAPI-e5185b8b-d62f-436a-9df7-442951ef2200"
    val url: String = "https://eun1.api.riotgames.com/lol/summoner/v4/summoners/by-name/Davidoski?api_key=RGAPI-b11ef8a1-1f98-4163-a13b-e6d58f47e0f2"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val layout = inflater.inflate(R.layout.fragment_search_screen, container, false)
        //val editSummoner: EditText = layout.findViewById(R.id.)
        //val textSummoner: TextView = layout.findViewById(R.id.textView)
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
        val buttonCalories = layout.findViewById<Button>(R.id.button)

        buttonCalories.setOnClickListener {
            val Summoner = editTextTextPersonName!!.text.toString()
            println(Summoner)

            //Start IO (input/output) coroutine for network operations
            CoroutineScope(IO).launch {
                fakeSummoner(Summoner)
            }
        }


//        CoroutineScope(IO).launch {
//            fakeSummoner()
//        }


        return layout
    }

    private fun setNewText(input: String){
//        val textSummoner: TextView = layout.(R.id.textView)
//        text.textView = apiResponse
//        layoutInflater.textView
        println("\n$input")
        textView.text = input
    }

    private fun setNewText2(input: String){
        println("\n$input")
        textView2.text = input
    }

    private suspend fun setTextOnMainThread(input: String, input2: String){
        //Start Main coroutine for operations on UI
        withContext(Main){
            setNewText(input)
            setNewText2("Global mastery score: $input2")
        }
    }

    private suspend fun fakeSummoner(input: String): String{
        val apiResponse = getSummoner(input)

        val json = JSONObject(apiResponse) // String instance holding the above json
        val summonerId = json.getString("id")
        println("summoner id hereeeeeeeeeeeeeeeeeeeeee $summonerId")

        val masteryScore = getMasteryScore(summonerId)

        setTextOnMainThread(apiResponse, masteryScore)



        return summonerId //testing here
    }

    private suspend fun getSummoner(input: String): String {
        logThread("getSummoner")
        //val apiResponse = URL("https://eun1.api.riotgames.com/lol/summoner/v4/summoners/by-name/Davidoski?api_key=RGAPI-d514c47e-e06f-4d8c-bd8a-503fef034e21").readText()
        return URL("https://eun1.api.riotgames.com/lol/summoner/v4/summoners/by-name/$input?api_key=RGAPI-d514c47e-e06f-4d8c-bd8a-503fef034e21").readText()
        //setTextOnMainThread(apiResponse)
    }

    private suspend fun getMasteryScore(input: String): String {
        return URL("https://eun1.api.riotgames.com/lol/champion-mastery/v4/scores/by-summoner/$input?api_key=RGAPI-d514c47e-e06f-4d8c-bd8a-503fef034e21").readText()
    }

    //function to log current thread where operation is taking place
    //Can be deleted later
    private fun logThread(methodName: String) {
        println("debug: ${methodName}: ${Thread.currentThread().name}")
    }
}
