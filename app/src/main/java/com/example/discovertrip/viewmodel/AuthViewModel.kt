package com.example.discovertrip.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.discovertrip.data.local.SessionManager
import com.example.discovertrip.data.model.LoginRequest
import com.example.discovertrip.data.model.LoginResponse
import com.example.discovertrip.data.remote.RetrofitClient
import com.example.discovertrip.data.repository.AuthRepository
import com.example.discovertrip.utils.Resource
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val authRepository = AuthRepository(
        RetrofitClient.authApiService,
        SessionManager(application)
    )

    private val _loginState = MutableLiveData<Resource<LoginResponse>>()
    val loginState: LiveData<Resource<LoginResponse>> get() = _loginState

    private val _authenticatedUser = MutableLiveData<Boolean>()
    val authenticatedUser: LiveData<Boolean> get() = _authenticatedUser

    init {
        checkSession()
    }

    fun checkSession() {
        _authenticatedUser.value = authRepository.isUserLoggedIn()
    }

    fun login(request: LoginRequest) {
        _loginState.value = Resource.Loading()
        viewModelScope.launch {
            val result = authRepository.login(request)
            _loginState.value = result
            if (result is Resource.Success) {
                _authenticatedUser.value = true
            }
        }
    }

    fun register(request: LoginRequest) {
        _loginState.value = Resource.Loading()
        viewModelScope.launch {
            val result = authRepository.register(request)
            _loginState.value = result
            if (result is Resource.Success) {
                _authenticatedUser.value = true
            }
        }
    }

    fun logout() {
        authRepository.logout()
        _authenticatedUser.value = false
    }
}
