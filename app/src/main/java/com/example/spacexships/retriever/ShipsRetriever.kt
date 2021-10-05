package com.example.spacexships.retriever

import com.example.spacexships.api.DataAPI
import com.example.spacexships.data.LaunchData
import com.example.spacexships.data.ShipsData
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ShipsRetriever {
    private val service: DataAPI
    companion object {
        const val BASE_URL = "https://api.spacexdata.com/v4/"
    }

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(DataAPI::class.java)
    }

    suspend fun getShips(): ArrayList<ShipsData> =
        service.getShipsAPI()

    suspend fun getLaunchData(id: String): LaunchData =
        service.getLaunchDataAPI(id)
}