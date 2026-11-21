package tn.rnu.isetr.projetntegrationmobile.util

// Les 3 états possibles d'une opération : chargement, succès, erreur
sealed class Resource<out T> {
    object Loading : Resource<Nothing>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(
        val message: String,
        val code: Int? = null,
        val fieldErrors: List<String> = emptyList()
    ) : Resource<Nothing>()
}