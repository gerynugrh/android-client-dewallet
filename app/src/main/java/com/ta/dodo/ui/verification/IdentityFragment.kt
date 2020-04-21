package com.ta.dodo.ui.verification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ta.dodo.databinding.IdentityFragmentBinding

class IdentityFragment : Fragment() {

    companion object {
        fun newInstance() = IdentityFragment()
    }

    private val identityViewModel: IdentityViewModel by lazy {
        ViewModelProvider(this).get(IdentityViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = IdentityFragmentBinding.inflate(inflater).apply {
            viewModel = identityViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}
