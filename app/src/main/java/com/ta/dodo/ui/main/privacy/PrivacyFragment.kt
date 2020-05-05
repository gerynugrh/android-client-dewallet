package com.ta.dodo.ui.main.privacy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider

import com.ta.dodo.databinding.PrivacyFragmentBinding

class PrivacyFragment : Fragment() {

    companion object {
        fun newInstance() = PrivacyFragment()
    }

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
}
