package tn.rnu.isetr.projetntegrationmobile.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import tn.rnu.isetr.projetntegrationmobile.MyApplication
import tn.rnu.isetr.projetntegrationmobile.data.remote.dto.RegisterRequest
import tn.rnu.isetr.projetntegrationmobile.databinding.FragmentRegisterBinding
import tn.rnu.isetr.projetntegrationmobile.util.Resource
import tn.rnu.isetr.projetntegrationmobile.util.Validators

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    // ViewModel partagé avec LoginFragment et MainActivity
    private val authViewModel: AuthViewModel by activityViewModels {
        AuthViewModelFactory((requireActivity().application as MyApplication).authRepository)
    }

    companion object {
        // À ADAPTER aux valeurs acceptées par le serveur (voir étape 7.0)
        private val CIVILITES = listOf(
            "Monsieur",
            "Madame",
            "Ne souhaite pas préciser")
        private val CENTRES_INTERET = listOf(
            "Randonnées", "Excursions", "Culture", "Soirées", "Loisirs", "Bons Plans"
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Liste déroulante de la civilité
        binding.editCivilite.setAdapter(
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, CIVILITES)
        )
        binding.editCivilite.setText(CIVILITES.last(), false)

        // Une puce cochable par centre d'intérêt
        CENTRES_INTERET.forEach { nom ->
            val chip = Chip(requireContext()).apply {
                id = View.generateViewId()
                text = nom
                isCheckable = true
            }
            binding.chipGroupInterets.addView(chip)
        }

        binding.buttonRegister.setOnClickListener {
            if (valider()) authViewModel.register(construireRequete())
        }
        binding.buttonBackLogin.setOnClickListener {
            findNavController().popBackStack()
        }

        observerInscription()
    }

    // Vérifie chaque champ et affiche l'erreur SOUS le champ concerné
    private fun valider(): Boolean {
        val mdp = binding.editPassword.text.toString()
        val erreurs = listOf(
            Validators.erreurNomPrenom(binding.editPrenom.text.toString())
                .also { binding.inputPrenom.error = it },
            Validators.erreurNomPrenom(binding.editNom.text.toString())
                .also { binding.inputNom.error = it },
            Validators.erreurEmail(binding.editEmail.text.toString())
                .also { binding.inputEmail.error = it },
            Validators.erreurTelephone(binding.editTelephone.text.toString())
                .also { binding.inputTelephone.error = it },
            Validators.erreurMotDePasse(mdp)
                .also { binding.inputPassword.error = it },
            Validators.erreurConfirmation(mdp, binding.editConfirm.text.toString())
                .also { binding.inputConfirm.error = it }
        )
        binding.textError.visibility = View.GONE
        return erreurs.all { it == null }
    }

    private fun construireRequete(): RegisterRequest {
        val interets = binding.chipGroupInterets.checkedChipIds.map { id ->
            binding.chipGroupInterets.findViewById<Chip>(id).text.toString()
        }
        return RegisterRequest(
            nom = binding.editNom.text.toString().trim(),
            prenom = binding.editPrenom.text.toString().trim(),
            email = binding.editEmail.text.toString().trim(),
            motDePasse = binding.editPassword.text.toString(),
            telephone = binding.editTelephone.text.toString().replace(" ", ""),
            civilite = binding.editCivilite.text.toString(),
            recevoirEmail = binding.checkEmail.isChecked,
            recevoirSms = binding.checkSms.isChecked,
            centresInteret = interets
        )
    }

    private fun observerInscription() {
        authViewModel.registerState.observe(viewLifecycleOwner) { etat ->
            when (etat) {
                null -> afficherChargement(false)
                Resource.Loading -> afficherChargement(true)
                is Resource.Success -> {
                    afficherChargement(false)
                    authViewModel.effacerRegisterState()
                    // Retour à la connexion : le message s'y affichera
                    findNavController().popBackStack()
                }
                is Resource.Error -> {
                    afficherChargement(false)
                    afficherErreurServeur(etat)
                    authViewModel.effacerRegisterState()
                }
            }
        }
    }

    private fun afficherErreurServeur(erreur: Resource.Error) {
        if (erreur.code == 409) {
            // E-mail déjà utilisé : le message va sous le champ e-mail
            binding.inputEmail.error = erreur.message
            binding.textError.visibility = View.GONE
        } else {
            // 400 : le message + la liste "errors" du serveur
            val details = (listOf(erreur.message) + erreur.fieldErrors).joinToString("\n")
            binding.textError.text = details
            binding.textError.visibility = View.VISIBLE
        }
    }

    private fun afficherChargement(enCours: Boolean) {
        binding.progressRegister.visibility = if (enCours) View.VISIBLE else View.GONE
        binding.buttonRegister.isEnabled = !enCours
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}