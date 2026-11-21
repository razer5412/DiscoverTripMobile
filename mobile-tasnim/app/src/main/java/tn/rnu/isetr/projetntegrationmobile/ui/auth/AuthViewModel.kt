package tn.rnu.isetr.projetntegrationmobile.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tn.rnu.isetr.projetntegrationmobile.data.local.UserEntity
import tn.rnu.isetr.projetntegrationmobile.data.remote.dto.RegisterRequest
import tn.rnu.isetr.projetntegrationmobile.data.repository.AuthRepository
import tn.rnu.isetr.projetntegrationmobile.util.Resource

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    // La session : null = déconnecté. MainActivity l'observe (critère II.2)
    val authenticatedUser: LiveData<UserEntity?> = repository.utilisateur

    // true quand le jeton a expiré
    val sessionExpiree: LiveData<Boolean> = repository.sessionExpiree

    // État de l'écran de connexion : chargement, succès ou erreur (null = rien en cours)
    private val _loginState = MutableLiveData<Resource<UserEntity>?>(null)
    val loginState: LiveData<Resource<UserEntity>?> = _loginState

    private val _registerState = MutableLiveData<Resource<String>?>()
    val registerState: LiveData<Resource<String>?> = _registerState

    private val _messageInfo = MutableLiveData<String?>()
    val messageInfo: LiveData<String?> = _messageInfo

    fun login(email: String, motDePasse: String) {
        _loginState.value = Resource.Loading
        viewModelScope.launch {
            _loginState.value = repository.login(email.trim(), motDePasse)
        }
    }

    fun register(requete: RegisterRequest) {
        _registerState.value = Resource.Loading
        viewModelScope.launch {
            val resultat = repository.register(requete)
            if (resultat is Resource.Success) {
                _messageInfo.value = "Compte créé avec succès. Vous pouvez maintenant vous connecter."
            }
            _registerState.value = resultat
        }
    }

    fun effacerRegisterState() {
        _registerState.value = null
    }

    fun messageInfoAffiche() {
        _messageInfo.value = null
    }

    fun logout() {
        viewModelScope.launch { repository.logout() }
    }

    // Appelée par l'écran après avoir lu le résultat
    fun effacerEtat() {
        _loginState.value = null
    }
}