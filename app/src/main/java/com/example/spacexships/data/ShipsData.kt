package com.example.spacexships.data

data class ShipsData(
    val home_port: String,
    val launches: List<String>,
    val name: String,
    val type: String,
    val image: String?
)