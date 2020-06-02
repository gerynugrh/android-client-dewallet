package com.ta.dodo.ui.register

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.ta.dodo.R
import com.ta.dodo.databinding.RecoverFragmentBinding

class RecoverFragment : Fragment() {

    companion object {
        fun newInstance() = RecoverFragment()
    }

    private val recoverViewModel: RecoverViewModel by lazy {
        ViewModelProvider(this).get(RecoverViewModel::class.java)
    }

    private lateinit var viewModel: RecoverViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = RecoverFragmentBinding.inflate(inflater).apply {
            viewModel = recoverViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recoverViewModel.recoverState.observe(viewLifecycleOwner, Observer {
            when(it) {
                RecoverState.MERGE -> navigateToRegisterWithSource()
                RecoverState.REGISTER -> navigateToRegister()
            }
        })
    }

    private fun navigateToRegisterWithSource() {
        val action = RecoverFragmentDirections.actionRecoverFragmentToRegisterFragment(
            originAccount = viewModel.privateKey.value
        )
        findNavController().navigate(action)
    }

    private fun navigateToRegister() {
        findNavController().navigate(R.id.registerFragment)
    }
}
