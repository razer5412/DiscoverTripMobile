package tn.rnu.isetr.projetntegrationmobile

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import tn.rnu.isetr.projetntegrationmobile.databinding.ActivityMainBinding
import tn.rnu.isetr.projetntegrationmobile.ui.auth.AuthViewModel
import tn.rnu.isetr.projetntegrationmobile.ui.auth.AuthViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    // Le même AuthViewModel est partagé avec les Fragments (activityViewModels)
    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory((application as MyApplication).authRepository)
    }

    // Écrans accessibles sans être connecté
    private val ecransPublics = setOf(R.id.loginFragment, R.id.registerFragment)

    private var roleAffiche: String? = null
    private var sessionChargee = false   // true dès que Room a répondu une première fois

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHost.navController
        binding.bottomNav.setupWithNavController(navController)

        // Écrans protégés + barre du bas visible seulement hors connexion/inscription
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val estPublic = destination.id in ecransPublics
            if (sessionChargee && authViewModel.authenticatedUser.value == null && !estPublic) {
                allerVers(R.id.loginFragment)
            }
            binding.bottomNav.visibility = if (estPublic) View.GONE else View.VISIBLE
        }

        // La session : null = déconnecté, sinon on adapte l'application au rôle
        authViewModel.authenticatedUser.observe(this) { user ->
            sessionChargee = true
            val destinationActuelle = navController.currentDestination?.id
            if (user == null) {
                roleAffiche = null
                if (destinationActuelle !in ecransPublics) allerVers(R.id.loginFragment)
            } else {
                if (roleAffiche != user.role) {
                    roleAffiche = user.role
                    binding.bottomNav.menu.clear()
                    binding.bottomNav.inflateMenu(
                        if (user.role == "planificateur") R.menu.menu_planificateur
                        else R.menu.menu_internaute
                    )
                }
                // Après connexion (ou réouverture de l'app) : écran d'accueil du rôle
                if (destinationActuelle in ecransPublics) {
                    allerVers(
                        if (user.role == "planificateur") R.id.receivedReservationsFragment
                        else R.id.explorerFragment
                    )
                }
            }
        }

        // Message quand le jeton a expiré (le serveur a répondu 401)
        authViewModel.sessionExpiree.observe(this) { expiree ->
            if (expiree) {
                Snackbar.make(
                    binding.root,
                    "Votre session a expiré. Veuillez vous reconnecter.",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    // Navigue vers un écran en vidant la pile (le bouton Retour ne revient pas en arrière)
    private fun allerVers(destination: Int) {
        val options = NavOptions.Builder()
            .setPopUpTo(navController.graph.id, true)
            .build()
        navController.navigate(destination, null, options)
    }
}