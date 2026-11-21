package tn.rnu.isetr.projetntegrationmobile.util

object Validators {

    private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    // --- Règles (vrai / faux) ---

    fun emailValide(email: String): Boolean = EMAIL_REGEX.matches(email.trim())

    // 8 caractères minimum, avec au moins une lettre et un chiffre
    fun motDePasseValide(motDePasse: String): Boolean =
        motDePasse.length >= 8 &&
                motDePasse.any { it.isLetter() } &&
                motDePasse.any { it.isDigit() }

    fun confirmationValide(motDePasse: String, confirmation: String): Boolean =
        motDePasse == confirmation

    // Facultatif : vide = valide ; sinon 8 à 15 chiffres (les espaces sont ignorés)
    fun telephoneValide(telephone: String): Boolean {
        val nettoye = telephone.replace(" ", "")
        if (nettoye.isEmpty()) return true
        return nettoye.all { it.isDigit() } && nettoye.length in 8..15
    }

    // --- Messages d'erreur (null = champ valide), affichés sous chaque champ ---

    fun erreurNomPrenom(valeur: String): String? =
        if (valeur.isBlank()) "Ce champ est obligatoire." else null

    fun erreurEmail(email: String): String? = when {
        email.isBlank() -> "L'adresse e-mail est obligatoire."
        !emailValide(email) -> "Adresse e-mail invalide."
        else -> null
    }

    fun erreurMotDePasse(motDePasse: String): String? = when {
        motDePasse.isEmpty() -> "Le mot de passe est obligatoire."
        !motDePasseValide(motDePasse) ->
            "Au moins 8 caractères, avec une lettre et un chiffre."
        else -> null
    }

    fun erreurConfirmation(motDePasse: String, confirmation: String): String? =
        if (!confirmationValide(motDePasse, confirmation))
            "Les mots de passe ne correspondent pas." else null

    fun erreurTelephone(telephone: String): String? =
        if (!telephoneValide(telephone))
            "Le téléphone doit contenir entre 8 et 15 chiffres." else null
}