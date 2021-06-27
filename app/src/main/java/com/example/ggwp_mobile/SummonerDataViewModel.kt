package com.example.ggwp_mobile

import androidx.lifecycle.ViewModel

class SummonerDataViewModel : ViewModel() {

    //place for changing api key
    private val key: String = "RGAPI-9ba0cb31-08cf-41dd-89b6-d7da1ded4061"

    private var puuId: String = ""
    private var summonerName: String = ""
    private var summonerId: String = ""
    private var summonerLevel: String = ""
    private var summonerIcon: String = ""

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

    fun updateSummonerLevel(input: String) {
        summonerLevel = input
    }

    fun returnSummonerLevel(): String {
        return summonerLevel
    }

    fun updateSummonerIcon(input: String) {
        summonerIcon = input
    }

    fun returnSummonerIcon(): String {
        return summonerIcon
    }
}