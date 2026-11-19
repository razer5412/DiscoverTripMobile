package com.example.discovertrip.data.repository

import com.example.discovertrip.data.model.Trip
import com.example.discovertrip.data.remote.TripApiService
import com.example.discovertrip.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TripRepository(
    private val tripApiService: TripApiService
) {
    suspend fun getTrips(): Resource<List<Trip>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = tripApiService.getTrips()
                if (response.isSuccessful && response.body() != null) {
                    Resource.Success(response.body()!!)
                } else {
                    Resource.Error(response.errorBody()?.string() ?: "Failed to fetch trips")
                }
            } catch (e: Exception) {
                Resource.Error(e.localizedMessage ?: "Network error")
            }
        }
    }

    suspend fun createTrip(trip: Trip): Resource<Trip> {
        return withContext(Dispatchers.IO) {
            try {
                val response = tripApiService.createTrip(trip)
                if (response.isSuccessful && response.body() != null) {
                    Resource.Success(response.body()!!)
                } else {
                    Resource.Error(response.errorBody()?.string() ?: "Failed to create trip")
                }
            } catch (e: Exception) {
                Resource.Error(e.localizedMessage ?: "Network error")
            }
        }
    }

    suspend fun updateTrip(id: String, trip: Trip): Resource<Trip> {
        return withContext(Dispatchers.IO) {
            try {
                val response = tripApiService.updateTrip(id, trip)
                if (response.isSuccessful && response.body() != null) {
                    Resource.Success(response.body()!!)
                } else {
                    Resource.Error(response.errorBody()?.string() ?: "Failed to update trip")
                }
            } catch (e: Exception) {
                Resource.Error(e.localizedMessage ?: "Network error")
            }
        }
    }

    suspend fun deleteTrip(id: String): Resource<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = tripApiService.deleteTrip(id)
                if (response.isSuccessful) {
                    Resource.Success(Unit)
                } else {
                    Resource.Error(response.errorBody()?.string() ?: "Failed to delete trip")
                }
            } catch (e: Exception) {
                Resource.Error(e.localizedMessage ?: "Network error")
            }
        }
    }

    suspend fun searchTrips(query: String): Resource<List<Trip>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = tripApiService.searchTrips(query)
                if (response.isSuccessful && response.body() != null) {
                    Resource.Success(response.body()!!)
                } else {
                    Resource.Error(response.errorBody()?.string() ?: "Search failed")
                }
            } catch (e: Exception) {
                Resource.Error(e.localizedMessage ?: "Network error")
            }
        }
    }
}
