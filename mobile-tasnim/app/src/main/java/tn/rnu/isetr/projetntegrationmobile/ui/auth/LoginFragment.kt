package tn.rnu.isetr.projetntegrationmobile.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import tn.rnu.isetr.projetntegrationmobile.MyApplication
import tn.rnu.isetr.projetntegrationmobile.R
import tn.rnu.isetr.projetntegrationmobile.databinding.FragmentLoginBinding
import tn.rnu.isetr.projetntegrationmobile.util.Resource
import tn.rnu.isetr.projetntegrationmobile.util.Validators

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    // ViewModel partagé avec MainActivity (même instance), créé par sa Factory
    private val authViewModel: AuthViewModel by activityViewModels {
        val app = requireActivity().application as MyApplication
        AuthViewModelFactory(app.authRepository)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonLogin.setOnClickListener { tenterConnexion() }

        // Le Fragment n'appelle que le ViewModel et OBSERVE son état
        authViewModel.loginState.observe(viewLifecycleOwner) { etat ->
            when (etat) {
                is Resource.Loading -> afficherChargement(true)
                is Resource.Success -> {
                    afficherChargement(false)
                    // La redirection vers l'accueil sera faite par MainActivity (étape 6)
                    Toast.makeText(requireContext(), "Connexion réussie", Toast.LENGTH_SHORT).show()
                    authViewModel.effacerEtat()
                }
                is Resource.Error -> {
                    afficherChargement(false)
                    binding.textError.text = etat.message
                    binding.textError.visibility = View.VISIBLE
                }
                null -> afficherChargement(false)
            }
        }
        binding.buttonGoRegister.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }
        authViewModel.messageInfo.observe(viewLifecycleOwner) { message ->
            if (message != null) {
                Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
                authViewModel.messageInfoAffiche()
            }
        }
    }

    private fun tenterConnexion() {
        val email = binding.editEmail.text.toString().trim()
        val motDePasse = binding.editPassword.text.toString()

        // Validation côté application : une erreur sous chaque champ
        binding.inputEmail.error = if (Validators.emailValide(email)) null else "Adresse e-mail invalide"
        binding.inputPassword.error = if (motDePasse.isNotEmpty()) null else "Le mot de passe est obligatoire"
        if (binding.inputEmail.error != null || binding.inputPassword.error != null) return

        binding.textError.visibility = View.GONE
        authViewModel.login(email, motDePasse)
    }

    private fun afficherChargement(enCours: Boolean) {
        binding.progressLogin.visibility = if (enCours) View.VISIBLE else View.GONE
        binding.buttonLogin.isEnabled = !enCours        // bouton désactivé pendant l'envoi
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}