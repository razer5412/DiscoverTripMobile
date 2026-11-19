package com.example.discovertrip.data.remote

import com.example.discovertrip.data.model.LoginRequest
import com.example.discovertrip.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body request: LoginRequest): Response<LoginResponse>
}
