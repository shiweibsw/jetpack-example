package com.android.jetpack.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        login.setOnClickListener {
            viewModel.authenticate(userNameEt.text.toString(), passwordEt.text.toString())
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.refuseAuthentication()
            findNavController().popBackStack(R.id.mainFragment, false)
        }

        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { status ->
            when (status) {
                LoginViewModel.AuthenticationState.AUTHENTICATED -> {
                    findNavController().popBackStack()
                }
                LoginViewModel.AuthenticationState.INVALID_AUTHENTCATION -> {
                    Toast.makeText(requireContext(), "登录失败", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
