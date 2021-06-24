package com.example.ggwp_mobile

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.fragment_chart_screen.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.URL

class ChartScreenFragment: Fragment() {

    private val viewModel: SummonerDataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_chart_screen, container, false)

        val key = viewModel.returnKey()
        val summonerId = viewModel.returnSummonerId()



        CoroutineScope(IO).launch {
            val statsMap = fakeStats(summonerId, key)
            withContext(Main){
                statsMap.get(64)
                val pieChart = layout.findViewById<PieChart>(R.id.pieChart)
                val visitiors: MutableList<PieEntry> = ArrayList()
                for ((key, value) in statsMap){
                    visitiors.add(PieEntry(value.toFloat(), key))
                }
//                visitiors.add(PieEntry(statsMap.get(64)!!.toFloat(), "Gibraltar"))
//                visitiors.add(PieEntry(2710f, "Czech"))
//                visitiors.add(PieEntry(1739f, "USA"))
//                visitiors.add(PieEntry(1704f, "Poland"))
//                visitiors.add(PieEntry(1653f, "Spain"))
//                visitiors.add(PieEntry(1528f, "France"))
//                visitiors.add(PieEntry(982f, "Germany"))
//                visitiors.add(PieEntry(920f, "Greece"))
//                visitiors.add(PieEntry(732f, "Russia"))

                val pieDataSet = PieDataSet(visitiors, "Covid infections per 1 million")
                pieDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)
                pieDataSet.valueTextColor = Color.BLACK
                pieDataSet.valueTextSize = 16f

                val pieData = PieData(pieDataSet)

                pieChart.data = pieData
                pieChart.description.isEnabled = false
                pieChart.centerText = "COVID"
                pieChart.animate()
            }
        }

        //val pieChart = layout.findViewById<PieChart>(R.id.pieChart)

//        val visitiors: MutableList<PieEntry> = ArrayList()
//        visitiors.add(PieEntry(2789f, "Gibraltar"))
//        visitiors.add(PieEntry(2710f, "Czech"))
//        visitiors.add(PieEntry(1739f, "USA"))
//        visitiors.add(PieEntry(1704f, "Poland"))
//        visitiors.add(PieEntry(1653f, "Spain"))
//        visitiors.add(PieEntry(1528f, "France"))
//        visitiors.add(PieEntry(982f, "Germany"))
//        visitiors.add(PieEntry(920f, "Greece"))
//        visitiors.add(PieEntry(732f, "Russia"))
//
//        val pieDataSet = PieDataSet(visitiors, "Covid infections per 1 million")
//        pieDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)
//        pieDataSet.valueTextColor = Color.BLACK
//        pieDataSet.valueTextSize = 16f
//
//        val pieData = PieData(pieDataSet)
//
//        pieChart.data = pieData
//        pieChart.description.isEnabled = false
//        pieChart.centerText = "COVID"
//        pieChart.animate()

        return layout
    }

    private fun fakeStats(input: String, input2: String): Map<Int, Int>{
        val stats = getChampionMastery(input, input2)

        //val leagueEntries = getLeagueEntries(summonerId, apiKey)
        val statsJson = JSONArray(stats)
        val statsJsonObject = statsJson.getJSONObject(0)
        val champ = statsJsonObject.getString("championId")
        //print("sSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS$champ")
        var myMap = HashMap<Int, Int>()
        for (j in 0..100){
            val statsToIterate = statsJson.getJSONObject(j)
            println(statsToIterate.getString("championPoints"))
            val key = statsToIterate.getString("championId").toInt()
            val value = statsToIterate.getString("championPoints").toInt()
            myMap.put(key, value)
        }
        print("mapałęłęęłęłę"+myMap)
        //setTextOnMainThread(summonerInfo, masteryScore, summonerIcon, summonerLvl, tier)

        drawGraph()
        //print(stats)
        return myMap
    }

    private fun getChampionMastery(summonerId: String, apiKey: String): String
    {
        return URL("https://eun1.api.riotgames.com/lol/champion-mastery/v4/champion-masteries/by-summoner/$summonerId?api_key=$apiKey").readText()
    }

    private fun drawGraph(){
        //val pieChart = layout.findViewById<PieChart>(R.id.pieChart)
        val pieChart = pieChart

        val visitiors: MutableList<PieEntry> = ArrayList()
        visitiors.add(PieEntry(2789f, "Gibraltar"))
        visitiors.add(PieEntry(2710f, "Czech"))
        visitiors.add(PieEntry(1739f, "USA"))
        visitiors.add(PieEntry(1704f, "Poland"))
        visitiors.add(PieEntry(1653f, "Spain"))
        visitiors.add(PieEntry(1528f, "France"))
        visitiors.add(PieEntry(982f, "Germany"))
        visitiors.add(PieEntry(920f, "Greece"))
        visitiors.add(PieEntry(732f, "Russia"))

        val pieDataSet = PieDataSet(visitiors, "Covid infections per 1 million")
        pieDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)
        pieDataSet.valueTextColor = Color.BLACK
        pieDataSet.valueTextSize = 16f

        val pieData = PieData(pieDataSet)

        pieChart.data = pieData
        pieChart.description.isEnabled = false
        pieChart.centerText = "COVID"
        pieChart.animate()
    }
}