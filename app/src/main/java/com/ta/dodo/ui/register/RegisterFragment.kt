package com.ta.dodo.ui.register

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ta.dodo.databinding.RegisterFragmentBinding
import mu.KotlinLogging
import com.ta.dodo.MainActivity
import com.ta.dodo.R
import com.ta.dodo.VerificationActivity
import kotlinx.android.synthetic.main.register_fragment.view.*
import java.io.FileNotFoundException

private val logger = KotlinLogging.logger {}

class RegisterFragment : Fragment() {

    companion object {
        fun newInstance() = RegisterFragment()
    }

    private val registerViewModel: RegisterViewModel by lazy {
        ViewModelProvider(this).get(RegisterViewModel::class.java)
    }

    private lateinit var registerButton: Button
    private lateinit var continueButton: Button
    private lateinit var privateKeyTooltip: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = RegisterFragmentBinding.inflate(inflater).apply {
            viewModel = registerViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        registerViewModel.registerState.observe(viewLifecycleOwner, Observer {
            when(it) {
                RegisterState.RECOVER -> navigateToRecoverAccount()
                RegisterState.FINISH -> {
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                }
            }
        })

        return binding.root
    }

    private fun navigateToRecoverAccount() {
        findNavController().navigate(R.id.recoverFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerButton = view.findViewById(R.id.btn_register)
        continueButton = view.findViewById(R.id.btn_continue)
        privateKeyTooltip = view.findViewById(R.id.et_private_key)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        try {
            registerViewModel.loadSavedPrivateKey()
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        } catch (ex: FileNotFoundException) {
            logger.info { "No saved private key detected" }
        } catch (ex: UninitializedPropertyAccessException) {
            logger.info { "Secret key hasn't been initialized" }
        }
    }

}
