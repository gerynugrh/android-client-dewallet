package com.ta.dodo.ui.main.send

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton
import com.davidmiguel.numberkeyboard.NumberKeyboard

import com.ta.dodo.R
import com.ta.dodo.databinding.SetAmountFragmentBinding

class SetAmountFragment : Fragment() {
    private val setAmountViewModel: SetAmountViewModel by lazy {
        ViewModelProvider(this).get(SetAmountViewModel::class.java)
    }

    private val args: SetAmountFragmentArgs by navArgs()

    private lateinit var setAmountKeyboard: NumberKeyboard
    private lateinit var confirmButton: CircularProgressButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = SetAmountFragmentBinding.inflate(inflater).apply {
            viewModel = setAmountViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAmountKeyboard = view.findViewById(R.id.set_amount_keyboard)
        confirmButton = view.findViewById(R.id.bt_set_amount_confirm)
    }

    override fun onStart() {
        super.onStart()
        setConfirmButtonListener(confirmButton)
        setAmountKeyboard.setListener(setAmountViewModel)
        setAmountViewModel.apply {
            username.value = args.username
            publicKey.value = args.publicKey
        }
    }

    private fun setConfirmButtonListener(button: CircularProgressButton) {
        setAmountViewModel.transactionState.observe(viewLifecycleOwner, Observer {
            when(it) {
                Transaction.START -> button.startAnimation()
                Transaction.FINISHED -> {
                    button.revertAnimation()
                    findNavController().navigate(R.id.homeFragment)
                }
            }
        })
    }
}
