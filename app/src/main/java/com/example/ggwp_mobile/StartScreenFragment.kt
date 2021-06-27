package com.example.ggwp_mobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.compose.ui.text.toLowerCase
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_start_screen.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import org.json.JSONObject
import java.io.FileNotFoundException
import java.net.URL
import java.util.*


class StartScreenFragment : Fragment() {

    private val viewModel: SummonerDataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_start_screen, container, false)

        val apiKey = viewModel.returnKey()
        val buttonStart: Button = layout.findViewById(R.id.buttonStart)

        val regionSpinner: Spinner = layout.findViewById(R.id.region_spinner)

        createRegionSpinner(layout, regionSpinner)

        viewModel.updateSummonerName("")

        buttonStart.setOnClickListener { view ->

            CoroutineScope(IO).launch {
                getSummonerBasicInformation(apiKey, editTextTextPersonName2.text.toString(), regionSpinner.selectedItem.toString())

                withContext(Main)
                {
                    if (viewModel.returnSummonerName() != "")
                    {
                        view.findNavController().navigate(R.id.SearchScreenFragment)
                        val bottomNavBarLayout = requireActivity().findViewById<CoordinatorLayout>(R.id.navbarLayout)
                        bottomNavBarLayout.visibility = View.VISIBLE
                    }
                }
            }
        }

        return layout
    }

    //gets summoner ids by summoner name (api request: summoner by name)
    private suspend fun getSummonerBasicInformation(apiKey: String, enteredSummonerName: String, summonerRegionCode: String)
    {
        val json: String

        try
        {
            json = URL("https://${summonerRegionCode.toLowerCase(Locale.ROOT)}.api.riotgames.com/lol/summoner/v4/summoners/by-name/$enteredSummonerName?api_key=$apiKey").readText()
        }
        catch (e: FileNotFoundException)
        {
            println("ERROR $e")
            withContext(Main)
            {

                Toast.makeText(context, "Summoner not found", Toast.LENGTH_SHORT).show()
            }

            return
        }

        val test = JSONObject(json)
        println(json)

        viewModel.updateSummonerID(test.getString("id"))
        viewModel.updatepuuId(test.getString("puuid"))
        viewModel.updateSummonerName(test.getString("name"))
        viewModel.updateSummonerLevel(test.getString("summonerLevel"))
        viewModel.updateSummonerIcon(test.get("profileIconId").toString())
        viewModel.updateSummonerRegionCode(summonerRegionCode)
    }

    private fun createRegionSpinner(layout: View, regionSpinner: Spinner)
    {
        ArrayAdapter.createFromResource(
            layout.context,
            R.array.regions_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            regionSpinner.adapter = adapter
            regionSpinner.setSelection(1)
        }
    }
}