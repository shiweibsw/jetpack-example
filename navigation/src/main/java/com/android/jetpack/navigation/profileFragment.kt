package com.android.jetpack.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_profile.*

class profileFragment : Fragment() {

    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { status ->
            when (status) {
                LoginViewModel.AuthenticationState.AUTHENTICATED -> {
                    welcomeTv.text = "欢迎您${viewModel.userName}"
                }
                LoginViewModel.AuthenticationState.UNAUTHENTCATED -> {
                    findNavController().navigate(R.id.loginFragment)
                }
            }
        })
    }
}
