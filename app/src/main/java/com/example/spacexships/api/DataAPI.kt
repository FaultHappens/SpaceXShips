package com.example.spacexships.api

import com.example.spacexships.data.LaunchData
import com.example.spacexships.data.ShipsData
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface DataAPI {
    @GET("ships")
    suspend fun getShipsAPI(): ArrayList<ShipsData>

    @GET
    suspend fun getLaunchDataAPI(@Url url: String): LaunchData
}

