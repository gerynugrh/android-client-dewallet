package com.ta.dodo.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.ta.dodo.R
import com.ta.dodo.databinding.HomeFragmentBinding

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    private lateinit var sendMoneyButton: ImageView
    private lateinit var transactionHistories: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = HomeFragmentBinding.inflate(inflater).apply {
            viewModel = homeViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sendMoneyButton = view.findViewById(R.id.iv_pay_icon)
        transactionHistories = view.findViewById(R.id.rv_transaction_history)

        transactionHistories.adapter = homeViewModel.transactionsHistoryAdapter
        transactionHistories.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        sendMoneyButton.setOnClickListener {
            findNavController().navigate(R.id.sendFragment)
        }
    }

    override fun onStart() {
        super.onStart()
        homeViewModel.refreshBalance()
        homeViewModel.refreshTransactions()
    }
}
