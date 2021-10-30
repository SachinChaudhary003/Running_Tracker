package com.example.runningtracker.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.runningtracker.R
import com.example.runningtracker.ui.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_setup.*

class SetupFragment : Fragment(R.layout.fragment_setup) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvContinue.setOnClickListener{
            findNavController().navigate(R.id.action_setupFragment_to_runFragments2)
        }
    }

}