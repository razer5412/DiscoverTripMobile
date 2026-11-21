package tn.rnu.isetr.projetntegrationmobile.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tn.rnu.isetr.projetntegrationmobile.data.local.UserDao
import tn.rnu.isetr.projetntegrationmobile.data.local.UserEntity
import tn.rnu.isetr.projetntegrationmobile.data.remote.AuthApi
import tn.rnu.isetr.projetntegrationmobile.data.remote.dto.LoginRequest
import tn.rnu.isetr.projetntegrationmobile.data.remote.dto.RegisterRequest
import tn.rnu.isetr.projetntegrationmobile.util.Resource
import tn.rnu.isetr.projetntegrationmobile.util.safeApiCall

class AuthRepository(
    private val api: AuthApi,
    private val userDao: UserDao
) {
    // La session : null = personne n'est connecté
    val utilisateur: LiveData<UserEntity?> = userDao.observeUser()

    // Passe à true quand le jeton a expiré (pour afficher un message)
    private val _sessionExpiree = MutableLiveData(false)
    val sessionExpiree: LiveData<Boolean> = _sessionExpiree

    suspend fun login(email: String, motDePasse: String): Resource<UserEntity> {
        val resultat = safeApiCall { api.login(LoginRequest(email, motDePasse)) }
        return when (resultat) {
            is Resource.Success -> {
                val reponse = resultat.data
                // Seuls l'internaute et le planificateur ont le droit d'utiliser le mobile
                if (reponse.role != "internaute" && reponse.role != "planificateur") {
                    Resource.Error("Ce compte n'est pas autorisé sur l'application mobile.")
                } else {
                    val dto = reponse.user
                    val idUtilisateur = dto.id
                    if (idUtilisateur.isNullOrBlank()) {
                        // Réponse inattendue du serveur : une erreur au lieu d'un plantage
                        Resource.Error("Réponse du serveur invalide.")
                    } else {
                        val user = UserEntity(
                            userId = idUtilisateur,
                            token = reponse.token,
                            role = reponse.role,
                            nom = dto.nom ?: "",
                            prenom = dto.prenom ?: "",
                            email = dto.email ?: ""
                        )
                        userDao.save(user)                    // la session est conservée
                        _sessionExpiree.postValue(false)
                        Resource.Success(user)
                    }
                }
            }
            is Resource.Error -> resultat                     // message du serveur (401, 403...)
            Resource.Loading -> Resource.Loading
        }
    }
    suspend fun register(requete: RegisterRequest): Resource<String> {
        val resultat = safeApiCall { api.register(requete) }
        return when (resultat) {
            is Resource.Success -> Resource.Success(resultat.data.message)
            is Resource.Error -> resultat          // 400 (liste errors), 409, réseau...
            Resource.Loading -> Resource.Loading
        }
    }

        // Déconnexion : on efface la session de Room
        suspend fun logout() {
            userDao.clear()
        }

        // Appelée par l'intercepteur quand le serveur répond 401
        fun signalerSessionExpiree() {
            _sessionExpiree.postValue(true)
        }
    }