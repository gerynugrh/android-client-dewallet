package com.ta.dodo.ui.main.send

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.ta.dodo.R

class SetAmountFragment : Fragment() {

    companion object {
        fun newInstance() = SetAmountFragment()
    }

    private lateinit var viewModel: SetAmountViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.set_amount_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SetAmountViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
