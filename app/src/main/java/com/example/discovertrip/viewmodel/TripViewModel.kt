package com.example.discovertrip.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.discovertrip.data.model.Trip
import com.example.discovertrip.data.remote.RetrofitClient
import com.example.discovertrip.data.repository.TripRepository
import com.example.discovertrip.utils.Resource
import kotlinx.coroutines.launch

class TripViewModel : ViewModel() {
    private val tripRepository = TripRepository(RetrofitClient.tripApiService)

    private val _tripsState = MutableLiveData<Resource<List<Trip>>>()
    val tripsState: LiveData<Resource<List<Trip>>> get() = _tripsState

    private val _tripOperationState = MutableLiveData<Resource<Any>>()
    val tripOperationState: LiveData<Resource<Any>> get() = _tripOperationState

    init {
        fetchTrips()
    }

    fun fetchTrips() {
        _tripsState.value = Resource.Loading()
        viewModelScope.launch {
            _tripsState.value = tripRepository.getTrips()
        }
    }

    fun createTrip(trip: Trip) {
        _tripOperationState.value = Resource.Loading()
        viewModelScope.launch {
            val result = tripRepository.createTrip(trip)
            _tripOperationState.value = result
            if (result is Resource.Success) {
                fetchTrips()
            }
        }
    }

    fun updateTrip(id: String, trip: Trip) {
        _tripOperationState.value = Resource.Loading()
        viewModelScope.launch {
            val result = tripRepository.updateTrip(id, trip)
            _tripOperationState.value = result
            if (result is Resource.Success) {
                fetchTrips()
            }
        }
    }

    fun deleteTrip(id: String) {
        _tripOperationState.value = Resource.Loading()
        viewModelScope.launch {
            val result = tripRepository.deleteTrip(id)
            _tripOperationState.value = result
            if (result is Resource.Success) {
                fetchTrips()
            }
        }
    }

    fun searchTrips(query: String) {
        _tripsState.value = Resource.Loading()
        viewModelScope.launch {
            _tripsState.value = tripRepository.searchTrips(query)
        }
    }
}
