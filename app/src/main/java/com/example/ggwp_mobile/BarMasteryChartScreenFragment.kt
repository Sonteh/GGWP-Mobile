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

        return layout
    }

    private fun fakeStats(input: String, input2: String): Map<String, Int>{
        val stats = getChampionMastery(input, input2)

        val statsJson = JSONArray(stats)

        var myMap = HashMap<String, Int>()
        for (j in 0..15){
            val statsToIterate = statsJson.getJSONObject(j)
            println(statsToIterate.getString("championPoints"))
            var keyId = statsToIterate.getString("championId").toInt()
            var key = getChampionName(keyId)
            val value = statsToIterate.getString("championPoints").toInt()
            myMap.put(key, value)
        }
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