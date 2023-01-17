package com.example.snappy.ui.login

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.snappy.R
import com.example.snappy.base.BaseFragment
import com.example.snappy.databinding.FragmentLoginBinding
import com.example.snappy.ui.MainActivity
import com.example.snappy.viewmodel.SharedViewModel
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig.*
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import kotlinx.coroutines.launch


class LoginFragment: BaseFragment<FragmentLoginBinding>() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }





    private fun checkLogin() {
//        if (viewModel.isUserLoggedIn()) {
//            navigateToHomeScreen()
//        } else {
//            clickListener()
//            serviceObserver()
//        }
    }

    private fun clickListener() {
        val userName = binding.editTextUserName
        val password = binding.editTextPassword

        binding.buttonLogin.setOnClickListener {
            if (userName.text.toString().isNotEmpty() && password.text.toString().isNotEmpty()) {
                lifecycleScope.launch {
                    viewModel.login(userName.text.toString(), password.text.toString())
                }
            } else {
                showMessage("Username and password should not be empty!")
            }
        }
    }

    private fun serviceObserver() {
        viewModel.responseLogin.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                com.example.snappy.data.APIResult.Status.SUCCESS -> {
                    showLoadingDialog(false)
                    result.data?.let {
                        if (it.status!!.lowercase() == "ok") {
                            showMessage("Login Successful!")
                            //viewModel.storePreference(it)
                            navigateToHomeScreen()
                        }
                    }
                }
                com.example.snappy.data.APIResult.Status.LOADING -> {
                    showLoadingDialog(true)
                }
                com.example.snappy.data.APIResult.Status.ERROR -> {
                    showLoadingDialog(false)
                }
            }
        }
    }

    private fun navigateToHomeScreen() {
        startActivity(Intent(requireContext(), MainActivity::class.java))
        requireActivity().finish()
    }
}