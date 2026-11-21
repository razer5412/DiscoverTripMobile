package tn.rnu.isetr.projetntegrationmobile.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// La table "user_table" : une seule ligne = l'utilisateur connecté
@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey val id: Int = 1,        // toujours 1 : il n'y a qu'une session
    val userId: String,                 // identifiant MongoDB de l'utilisateur
    val token: String,                  // le jeton envoyé avec chaque requête
    val role: String,                   // "internaute" ou "planificateur"
    val nom: String?,
    val prenom: String?,
    val email: String?
)