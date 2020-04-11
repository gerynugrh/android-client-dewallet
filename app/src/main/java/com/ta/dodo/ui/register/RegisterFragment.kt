package com.ta.dodo.ui.register

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.ta.dodo.databinding.RegisterFragmentBinding
import mu.KotlinLogging
import com.ta.dodo.MainActivity
import java.io.FileNotFoundException

private val logger = KotlinLogging.logger {}

class RegisterFragment : Fragment() {

    companion object {
        fun newInstance() = RegisterFragment()
    }

    private val registerViewModel: RegisterViewModel by lazy {
        ViewModelProvider(this).get(RegisterViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = RegisterFragmentBinding.inflate(inflater).apply {
            viewModel = registerViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        try {
            registerViewModel.loadSavedPrivateKey()
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        } catch (ex: FileNotFoundException) {
            logger.info { "No saved private key detected" }
        }
    }

}
