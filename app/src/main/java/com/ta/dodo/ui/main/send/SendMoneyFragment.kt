package com.ta.dodo.ui.main.send

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ta.dodo.R
import com.ta.dodo.databinding.SendMoneyFragmentBinding

class SendMoneyFragment : Fragment() {

    companion object {
        fun newInstance() = SendMoneyFragment()
    }

    private val sendMoneyViewModel: SendMoneyViewModel by lazy {
        ViewModelProvider(this).get(SendMoneyViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = SendMoneyFragmentBinding.inflate(inflater).apply {
            viewModel = sendMoneyViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        sendMoneyViewModel.isPublicKeyFetched.observe(viewLifecycleOwner, Observer {
            if (!it) {
                return@Observer
            }
            findNavController().navigate(R.id.setAmountFragment)
        })
    }
}
