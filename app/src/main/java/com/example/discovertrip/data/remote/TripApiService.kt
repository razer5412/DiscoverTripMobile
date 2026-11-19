package com.example.discovertrip.data.remote

import com.example.discovertrip.data.model.Trip
import retrofit2.Response
import retrofit2.http.*

interface TripApiService {
    @GET("trips")
    suspend fun getTrips(): Response<List<Trip>>

    @GET("trips/{id}")
    suspend fun getTripById(@Path("id") id: String): Response<Trip>

    @POST("trips")
    suspend fun createTrip(@Body trip: Trip): Response<Trip>

    @PUT("trips/{id}")
    suspend fun updateTrip(@Path("id") id: String, @Body trip: Trip): Response<Trip>

    @DELETE("trips/{id}")
    suspend fun deleteTrip(@Path("id") id: String): Response<Unit>

    @GET("trips/search")
    suspend fun searchTrips(@Query("query") query: String): Response<List<Trip>>
}
