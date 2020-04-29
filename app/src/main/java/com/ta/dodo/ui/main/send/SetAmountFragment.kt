package com.ta.dodo.ui.main.send

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.davidmiguel.numberkeyboard.NumberKeyboard

import com.ta.dodo.R
import com.ta.dodo.databinding.SetAmountFragmentBinding

class SetAmountFragment : Fragment() {

    companion object {
        fun newInstance() = SetAmountFragment()
    }

    private val setAmountViewModel: SetAmountViewModel by lazy {
        ViewModelProvider(this).get(SetAmountViewModel::class.java)
    }

    private lateinit var setAmountKeyboard: NumberKeyboard

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
        setAmountKeyboard.setListener(setAmountViewModel)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}
