package tn.rnu.isetr.projetntegrationmobile.util

import org.json.JSONObject
import retrofit2.Response
import java.io.IOException

// Exécute un appel Retrofit sans jamais planter : renvoie toujours un Resource
suspend fun <T> safeApiCall(call: suspend () -> Response<T>): Resource<T> {
    return try {
        val response = call()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) Resource.Success(body)
            else Resource.Error("Réponse vide du serveur", response.code())
        } else {
            lireErreurServeur(response)            // erreurs 4xx et 5xx
        }
    } catch (e: IOException) {
        Resource.Error("Impossible de joindre le serveur. Vérifiez votre connexion.")
    } catch (e: Exception) {
        Resource.Error("Une erreur inattendue est survenue.")
    }
}

// Le serveur répond toujours { message, errors } : on lit ce message
private fun lireErreurServeur(response: Response<*>): Resource.Error {
    return try {
        val json = JSONObject(response.errorBody()?.string() ?: "")
        val liste = json.optJSONArray("errors")
        val erreurs = if (liste == null) emptyList()
        else List(liste.length()) { liste.getString(it) }
        Resource.Error(json.optString("message", "Erreur ${response.code()}"), response.code(), erreurs)
    } catch (e: Exception) {
        Resource.Error("Erreur ${response.code()}", response.code())
    }
}