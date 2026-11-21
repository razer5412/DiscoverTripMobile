package tn.rnu.isetr.projetntegrationmobile

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tn.rnu.isetr.projetntegrationmobile.data.local.AppDatabase
import tn.rnu.isetr.projetntegrationmobile.data.remote.AuthInterceptor
import tn.rnu.isetr.projetntegrationmobile.data.remote.RetrofitClient
import tn.rnu.isetr.projetntegrationmobile.data.repository.AuthRepository

class MyApplication : Application() {

    val database by lazy { AppDatabase.getDatabase(this) }

    val authRepository by lazy {
        AuthRepository(RetrofitClient.authApi, database.userDao())
    }

    override fun onCreate() {
        super.onCreate()
        // Prépare l'intercepteur : si le jeton expire, on efface la session
        RetrofitClient.authInterceptor = AuthInterceptor(database.userDao()) {
            CoroutineScope(Dispatchers.IO).launch {
                authRepository.logout()
                authRepository.signalerSessionExpiree()
            }
        }
    }
}