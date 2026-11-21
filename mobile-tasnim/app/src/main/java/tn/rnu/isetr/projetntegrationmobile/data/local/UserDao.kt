package tn.rnu.isetr.projetntegrationmobile.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    // Enregistre la session (remplace la précédente grâce à REPLACE)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(user: UserEntity)

    // LiveData : l'écran est prévenu automatiquement quand la session change
    @Query("SELECT * FROM user_table LIMIT 1")
    fun observeUser(): LiveData<UserEntity?>

    // Lecture ponctuelle (utile pour récupérer le jeton)
    @Query("SELECT * FROM user_table LIMIT 1")
    suspend fun getUser(): UserEntity?

    // Déconnexion : on efface la session
    @Query("DELETE FROM user_table")
    suspend fun clear()
}