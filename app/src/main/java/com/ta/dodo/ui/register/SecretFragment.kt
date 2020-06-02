package com.ta.dodo.ui.register

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.ta.dodo.MainActivity

import com.ta.dodo.R
import com.ta.dodo.databinding.SecretFragmentBinding

class SecretFragment : Fragment() {

    companion object {
        fun newInstance() = SecretFragment()
    }

    private val secretViewModel: SecretViewModel by lazy {
        ViewModelProvider(this).get(SecretViewModel::class.java)
    }

    private val args: SecretFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = SecretFragmentBinding.inflate(inflater).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = secretViewModel
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        secretViewModel.secret.value = args.secret
        secretViewModel.viewModelState.observe(viewLifecycleOwner, Observer {
            when(it) {
                SecretViewModel.State.FINISH -> navigateToMainActivity()
            }
        })
    }

    private fun navigateToMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
    }
}
