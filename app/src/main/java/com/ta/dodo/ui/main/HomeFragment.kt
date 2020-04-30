package com.ta.dodo.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
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
    private lateinit var noTransaction: TextView
    private lateinit var transactionProgressBar: ProgressBar

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
        noTransaction = view.findViewById(R.id.tv_no_transaction)
        transactionProgressBar = view.findViewById(R.id.pb_transaction_loading)

        transactionHistories.adapter = homeViewModel.transactionsHistoryAdapter
        transactionHistories.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        sendMoneyButton.setOnClickListener {
            findNavController().navigate(R.id.sendFragment)
        }
        setTransactionHistoryState(noTransaction, transactionProgressBar)
    }

    private fun setTransactionHistoryState(text: TextView, progressBar: ProgressBar) {
        homeViewModel.historyTransactionState.observe(viewLifecycleOwner, Observer {
            when(it) {
                HistoryTransactionState.FINISHED -> {
                    text.visibility = View.GONE
                    progressBar.visibility = View.GONE
                }
                HistoryTransactionState.ERROR -> {
                    text.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
                HistoryTransactionState.START -> {
                    progressBar.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        homeViewModel.refreshScreen()
    }
}
