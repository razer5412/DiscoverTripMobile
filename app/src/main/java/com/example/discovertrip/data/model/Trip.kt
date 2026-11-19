package com.example.discovertrip.data.model

data class Trip(
    val id: String? = null,
    val title: String,
    val description: String,
    val destination: String,
    val price: Double,
    val imageUrl: String? = null,
    val startDate: String,
    val endDate: String
)
