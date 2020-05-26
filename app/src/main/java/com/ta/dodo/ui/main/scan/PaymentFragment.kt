package com.ta.dodo.ui.main.scan

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
import com.ta.dodo.R
import com.ta.dodo.databinding.PaymentFragmentBinding
import kotlinx.android.synthetic.main.payment_fragment.view.*

class PaymentFragment : Fragment() {
    private val paymentViewModel: PaymentViewModel by lazy {
        ViewModelProvider(this).get(PaymentViewModel::class.java)
    }

    private val args: PaymentFragmentArgs by navArgs()
    private lateinit var button: CircularProgressButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = PaymentFragmentBinding.inflate(inflater).apply {
            viewModel = paymentViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    private fun setPaymentStateListener() {
        paymentViewModel.paymentState.observe(viewLifecycleOwner, Observer {
            when(it) {
                PaymentState.SUCCESS -> {
                    button.revertAnimation()
                    navigateToHome()
                }
                PaymentState.START -> {
                    button.startAnimation()
                }
                PaymentState.ERROR -> {
                    button.revertAnimation()
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button = view.findViewById(R.id.bt_payment_confirm)
    }

    private fun navigateToHome() {
        findNavController().navigate(R.id.homeFragment)
    }

    override fun onStart() {
        super.onStart()
        paymentViewModel.apply {
            payment = args.payment
        }
        setPaymentStateListener()
    }
}
