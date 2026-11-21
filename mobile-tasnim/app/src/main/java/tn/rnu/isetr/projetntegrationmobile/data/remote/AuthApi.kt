package tn.rnu.isetr.projetntegrationmobile.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import tn.rnu.isetr.projetntegrationmobile.data.remote.dto.AuthResponse
import tn.rnu.isetr.projetntegrationmobile.data.remote.dto.LoginRequest
import tn.rnu.isetr.projetntegrationmobile.data.remote.dto.MessageResponse
import tn.rnu.isetr.projetntegrationmobile.data.remote.dto.RegisterRequest

interface AuthApi {
    // Pas de "/" au début : BASE_URL se termine déjà par "api/"
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("auth/register-internaute")
    suspend fun register(@Body request: RegisterRequest): Response<MessageResponse>
}