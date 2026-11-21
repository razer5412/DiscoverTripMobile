package tn.rnu.isetr.projetntegrationmobile.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import tn.rnu.isetr.projetntegrationmobile.MyApplication
import tn.rnu.isetr.projetntegrationmobile.R
import tn.rnu.isetr.projetntegrationmobile.databinding.FragmentPlaceholderBinding
import tn.rnu.isetr.projetntegrationmobile.ui.auth.AuthViewModel
import tn.rnu.isetr.projetntegrationmobile.ui.auth.AuthViewModelFactory

class ProfileFragment : Fragment(R.layout.fragment_placeholder) {

    private val authViewModel: AuthViewModel by activityViewModels {
        AuthViewModelFactory((requireActivity().application as MyApplication).authRepository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentPlaceholderBinding.bind(view)
        binding.textPlaceholder.text = "Compte (étape 8)"
        binding.buttonPlaceholder.visibility = View.VISIBLE
        binding.buttonPlaceholder.text = "Se déconnecter"
        binding.buttonPlaceholder.setOnClickListener { authViewModel.logout() }
    }
}