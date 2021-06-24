package com.example.ggwp_mobile

import androidx.lifecycle.ViewModel

class SummonerDataViewModel : ViewModel() {

    //place for changing api key
    private val key: String = "RGAPI-c27bf127-11b3-45f5-9b4c-741f6915284a"


    private var puuId: String = ""
    var summonerName: String = ""
    var summonerId: String = ""

    fun updatepuuId(input: String) {
        puuId = input
    }

    fun returnpuuId(): String {
        return puuId
    }

    fun updateSummonerID(input: String) {
        summonerId = input
    }

    fun returnSummonerId(): String {
        return summonerId
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