package com.ta.dodo.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider

import com.ta.dodo.R
import com.ta.dodo.databinding.SendFragmentBinding

class SendFragment : Fragment() {

    companion object {
        fun newInstance() = SendFragment()
    }

    private val sendViewModel: SendViewModel by lazy {
        ViewModelProvider(this).get(SendViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = SendFragmentBinding.inflate(inflater).apply {
            viewModel = sendViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}
