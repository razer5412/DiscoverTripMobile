package com.example.discovertrip.data.repository

import com.example.discovertrip.data.local.SessionManager
import com.example.discovertrip.data.model.LoginRequest
import com.example.discovertrip.data.model.LoginResponse
import com.example.discovertrip.data.remote.AuthApiService
import com.example.discovertrip.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(
    private val authApiService: AuthApiService,
    private val sessionManager: SessionManager
) {
    suspend fun login(request: LoginRequest): Resource<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = authApiService.login(request)
                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!
                    sessionManager.saveAuthToken(loginResponse.token)
                    sessionManager.saveUserInfo(loginResponse.user.email, loginResponse.user.name)
                    Resource.Success(loginResponse)
                } else {
                    Resource.Error(response.errorBody()?.string() ?: "Authentication failed")
                }
            } catch (e: Exception) {
                Resource.Error(e.localizedMessage ?: "Network error occurred")
            }
        }
    }

    suspend fun register(request: LoginRequest): Resource<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = authApiService.register(request)
                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!
                    sessionManager.saveAuthToken(loginResponse.token)
                    sessionManager.saveUserInfo(loginResponse.user.email, loginResponse.user.name)
                    Resource.Success(loginResponse)
                } else {
                    Resource.Error(response.errorBody()?.string() ?: "Registration failed")
                }
            } catch (e: Exception) {
                Resource.Error(e.localizedMessage ?: "Network error occurred")
            }
        }
    }

    fun logout() {
        sessionManager.logout()
    }

    fun isUserLoggedIn(): Boolean {
        return sessionManager.isLoggedIn()
    }
}
