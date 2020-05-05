package com.ta.dodo.ui.main.privacy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton
import com.ta.dodo.R

import com.ta.dodo.databinding.PrivacyFragmentBinding
import com.ta.dodo.toast

class PrivacyFragment : Fragment() {

    companion object {
        fun newInstance() = PrivacyFragment()
    }

    private lateinit var authorizeUserButton: CircularProgressButton
    private val args: PrivacyFragmentArgs by navArgs()

    private val privacyViewModel: PrivacyViewModel by lazy {
        ViewModelProvider(this).get(PrivacyViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = PrivacyFragmentBinding.inflate(inflater).apply {
            viewModel = privacyViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authorizeUserButton = view.findViewById(R.id.bt_authorize_user)
        privacyViewModel.greeting.value = "Halo ${args.identifier},"

        setAuthorizeButtonState(authorizeUserButton)
    }

    private fun setAuthorizeButtonState(button: CircularProgressButton) {
        privacyViewModel.authorizeUserState.observe(viewLifecycleOwner, Observer {
            when(it) {
                AuthorizeUserState.IDLE -> button.revertAnimation()
                AuthorizeUserState.START -> button.startAnimation()
                AuthorizeUserState.FINISHED -> {
                    button.revertAnimation()
                    requireContext().toast("Sukses memberikan izin pada user")
                }
            }
        })
    }
}
