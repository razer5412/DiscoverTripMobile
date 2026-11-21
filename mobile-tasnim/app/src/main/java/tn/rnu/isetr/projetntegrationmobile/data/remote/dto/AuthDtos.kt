package tn.rnu.isetr.projetntegrationmobile.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("motDePasse") val motDePasse: String
)

data class UserDto(
    @SerializedName("id") val id: String?,
    @SerializedName("nom") val nom: String?,
    @SerializedName("prenom") val prenom: String?,
    @SerializedName("email") val email: String?
)

data class AuthResponse(
    @SerializedName("token") val token: String,
    @SerializedName("role") val role: String,
    @SerializedName("user") val user: UserDto
)
data class RegisterRequest(
    @SerializedName("nom") val nom: String,
    @SerializedName("prenom") val prenom: String,
    @SerializedName("email") val email: String,
    @SerializedName("motDePasse") val motDePasse: String,
    @SerializedName("telephone") val telephone: String,
    @SerializedName("civilite") val civilite: String,
    @SerializedName("recevoirEmail") val recevoirEmail: Boolean,
    @SerializedName("recevoirSms") val recevoirSms: Boolean,
    @SerializedName("centresInteret") val centresInteret: List<String>
)

data class MessageResponse(
    @SerializedName("message") val message: String
)