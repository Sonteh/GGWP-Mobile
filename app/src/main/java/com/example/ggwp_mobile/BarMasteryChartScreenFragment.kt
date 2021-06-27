package com.example.ggwp_mobile

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class BarMasteryChartScreenFragment: Fragment() {

    private val viewModel: SummonerDataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_bar_mastery_chart_screen, container, false)

        val key = viewModel.returnKey()
        val summonerId = viewModel.returnSummonerId()

        CoroutineScope(IO).launch {
            val statsMap = fakeStats(summonerId, key)
//            for ((key, value) in statsMap){
//
//            }
            withContext(Main){
                statsMap.get(64)
                val barChart = layout.findViewById<HorizontalBarChart>(R.id.horizontalChart)
                val visitiors: MutableList<BarEntry> = ArrayList()
                var i = 0
                val xLabel: ArrayList<String> = ArrayList()
                for ((key, value) in statsMap){
                    visitiors.add(BarEntry(i.toFloat(), value.toFloat()))
                    xLabel.add("$key");
                    i++
                }
                visitiors.add(BarEntry(0f, 0f))
//                visitiors.add(BarEntry(1f, 16263695f))
//                visitiors.add(BarEntry(2f, 3180810f))
//                visitiors.add(BarEntry(3f, 2751632f))
//                visitiors.add(PieEntry(statsMap.get(64)!!.toFloat(), "Gibraltar"))
//                visitiors.add(PieEntry(2710f, "Czech"))
//                visitiors.add(PieEntry(1739f, "USA"))
//                visitiors.add(PieEntry(1704f, "Poland"))
//                visitiors.add(PieEntry(1653f, "Spain"))
//                visitiors.add(PieEntry(1528f, "France"))
//                visitiors.add(PieEntry(982f, "Germany"))
//                visitiors.add(PieEntry(920f, "Greece"))
//                visitiors.add(PieEntry(732f, "Russia"))

                val xAxis: XAxis = barChart.xAxis
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)
                xAxis.granularity = 1f
                xAxis.valueFormatter = object: ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return xLabel[value.toInt()]
                    }
                }

                val barDataSet = BarDataSet(visitiors, "Champions by mastery points")
                barDataSet.setColors(*ColorTemplate.VORDIPLOM_COLORS)
                barDataSet.valueTextColor = Color.BLACK
                barDataSet.valueTextSize = 8f

                val barData = BarData(barDataSet)

                barChart.setFitBars(false)
                barChart.data = barData
                barChart.description.text = "Mastery points"
                barChart.animateY(2000)
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
        for (j in 0..15){
            val statsToIterate = statsJson.getJSONObject(j)
            println(statsToIterate.getString("championPoints"))
            val key = statsToIterate.getString("championId").toInt()
            val value = statsToIterate.getString("championPoints").toInt()
            myMap.put(key, value)
        }
        print("mapałęłęęłęłę"+myMap)
        //setTextOnMainThread(summonerInfo, masteryScore, summonerIcon, summonerLvl, tier)
        //print(stats)
        return myMap.entries.sortedBy { it.value }.associate { it.toPair() }
    }

    private fun getChampionMastery(summonerId: String, apiKey: String): String
    {
        return URL("https://eun1.api.riotgames.com/lol/champion-mastery/v4/champion-masteries/by-summoner/$summonerId?api_key=$apiKey").readText()
    }

    private fun getChampionName(id: Int): String{
        val name = URL("https://api.hiray.me/lol/pbe/default/ids.json").readText()

        val nameJSON = JSONObject(name)

        return nameJSON.getString("$id")
    }
}