package com.example.ggwp_mobile

import androidx.lifecycle.ViewModel

class SummonerDataViewModel : ViewModel() {

    //place for changing api key
    private val key: String = "RGAPI-ab0c2c24-5686-44c1-bf8d-565df60b8de1"

    private var puuId: String = ""
    var summonerName: String = ""

    fun updatepuuId(input: String) {
        puuId = input
    }

    fun returnpuuId(): String {
        return puuId
    }

    fun updateSummonerName(input: String) {
        summonerName = input
    }

    fun returnSummonerName(): String {
        return summonerName
    }

    fun returnKey(): String {
        return key
    }
}