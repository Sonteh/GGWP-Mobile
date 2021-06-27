package com.example.ggwp_mobile

import androidx.lifecycle.ViewModel

class SummonerDataViewModel : ViewModel() {

    //place for changing api key
    private val key: String = "RGAPI-62fc213c-9a4f-4175-a969-a713c6702844"

    private var puuId: String = ""
    private var summonerName: String = ""
    private var summonerId: String = ""
    private var summonerLevel: String = ""
    private var summonerIcon: String = ""
    private var summonerRegionCode: String = ""
    private var summonerRegion: String = ""

    fun updatepuuId(input: String)
    {
        puuId = input
    }

    fun returnpuuId(): String
    {
        return puuId
    }

    fun updateSummonerID(input: String)
    {
        summonerId = input
    }

    fun returnSummonerId(): String
    {
        return summonerId
    }

    fun updateSummonerName(input: String)
    {
        summonerName = input
    }

    fun returnSummonerName(): String
    {
        return summonerName
    }

    fun returnKey(): String
    {
        return key
    }

    fun updateSummonerLevel(input: String)
    {
        summonerLevel = input
    }

    fun returnSummonerLevel(): String
    {
        return summonerLevel
    }

    fun updateSummonerIcon(input: String)
    {
        summonerIcon = input
    }

    fun returnSummonerIcon(): String
    {
        return summonerIcon
    }

    fun updateSummonerRegionCode(input: String)
    {
        summonerRegionCode = input

        if (summonerRegionCode == "EUN1" || summonerRegionCode == "EUW1" || summonerRegionCode == "RU" ||
                summonerRegionCode == "TR")
        {
            summonerRegion = "EUROPE"
        }

        if (summonerRegionCode == "LA1" || summonerRegionCode == "LA2" || summonerRegionCode == "NA1" ||
            summonerRegionCode == "OC1")
        {
            summonerRegion = "AMERICAS"
        }

        if (summonerRegionCode == "JP1" || summonerRegionCode == "KR")
        {
            summonerRegion = "ASIA"
        }
    }

    fun returnSummonerRegionCode(): String
    {
        return summonerRegionCode
    }

    fun returnSummonerRegion(): String
    {
        return summonerRegion
    }
}