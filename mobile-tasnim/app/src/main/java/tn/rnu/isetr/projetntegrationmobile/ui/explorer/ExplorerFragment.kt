package tn.rnu.isetr.projetntegrationmobile.ui.explorer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import tn.rnu.isetr.projetntegrationmobile.R
import tn.rnu.isetr.projetntegrationmobile.databinding.FragmentPlaceholderBinding

class ExplorerFragment : Fragment(R.layout.fragment_placeholder) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FragmentPlaceholderBinding.bind(view).textPlaceholder.text = "Explorer (Fatma)"
    }
}