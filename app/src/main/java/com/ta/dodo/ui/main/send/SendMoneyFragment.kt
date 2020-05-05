package com.ta.dodo.ui.main.send

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton
import com.ta.dodo.R
import com.ta.dodo.databinding.SendMoneyFragmentBinding

class SendMoneyFragment : Fragment() {
    private val sendMoneyViewModel: SendMoneyViewModel by lazy {
        ViewModelProvider(this).get(SendMoneyViewModel::class.java)
    }

    private lateinit var sendMoneyButton: CircularProgressButton

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sendMoneyButton = view.findViewById(R.id.bt_send_money)
    }

    override fun onStart() {
        super.onStart()
        setSendMoneyButtonListener(sendMoneyButton)
    }

    private fun setSendMoneyButtonListener(button: CircularProgressButton) {
        sendMoneyViewModel.searchPublicKeyState.observe(viewLifecycleOwner, Observer {
            when(it) {
                SearchPublicKey.START -> sendMoneyButton.startAnimation()
                SearchPublicKey.FINISHED -> {
                    sendMoneyButton.revertAnimation()
                    navigateToSetAmount()
                }
            }
        })
    }

    private fun navigateToSetAmount() {
        val action = SendMoneyFragmentDirections.actionSendFragmentToSetAmountFragment(
            username = sendMoneyViewModel.identifier,
            publicKey = sendMoneyViewModel.publicKey
        )
        findNavController().navigate(action)
    }
}
