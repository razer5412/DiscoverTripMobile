package tn.rnu.isetr.projetntegrationmobile.data.remote

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import tn.rnu.isetr.projetntegrationmobile.data.local.UserDao

class AuthInterceptor(
    private val userDao: UserDao,
    private val onUnauthorized: () -> Unit       // appelée si le jeton a expiré
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        // L'intercepteur tourne déjà en arrière-plan : on peut lire Room ici
        val token = runBlocking { userDao.getUser()?.token }

        val requete = chain.request().newBuilder().apply {
            if (token != null) addHeader("Authorization", "Bearer $token")
        }.build()

        val reponse = chain.proceed(requete)

        // 401 alors qu'on avait un jeton = jeton expiré
        // (une connexion ratée n'a pas de jeton, elle ne déclenche donc rien)
        if (reponse.code == 401 && token != null) onUnauthorized()

        return reponse
    }
}