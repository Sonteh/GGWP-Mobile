package com.example.ggwp_mobile

import androidx.lifecycle.ViewModel

class SummonerDataViewModel : ViewModel() {

    //place for changing api key
    private val key: String = "RGAPI-3aa63050-3900-4308-b2f9-1f037581cb5f"


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