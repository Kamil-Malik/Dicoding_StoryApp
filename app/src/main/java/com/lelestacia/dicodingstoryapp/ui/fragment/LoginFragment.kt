package com.lelestacia.dicodingstoryapp.ui.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.lelestacia.dicodingstoryapp.R
import com.lelestacia.dicodingstoryapp.databinding.FragmentLoginBinding
import com.lelestacia.dicodingstoryapp.ui.activity.MainActivity
import com.lelestacia.dicodingstoryapp.ui.viewmodel.MainViewModel
import com.lelestacia.dicodingstoryapp.utility.NetworkResponse
import com.lelestacia.dicodingstoryapp.utility.Utility
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.edtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                return
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().isEmpty()) {
                    binding.btnLogin.isEnabled = false
                } else if (p0.toString().length < 6) {
                    binding.edtPassword.error = "Password minimal adalah 6 huruf"
                } else if (p0.toString().length >= 6)
                    binding.btnLogin.isEnabled = true
            }

            override fun afterTextChanged(p0: Editable?) {
                return
            }
        })

        binding.btnLogin.setOnClickListener(this)
        binding.tvRegister.setOnClickListener(this)
        subscribe()
    }

    private fun subscribe() {
        viewModel.loginInfo.observe(viewLifecycleOwner) { loginInformation ->
            when (loginInformation) {
                is NetworkResponse.GenericException -> Toast.makeText(
                    context,
                    "Error : ${loginInformation.code}, ${loginInformation.cause}",
                    Toast.LENGTH_SHORT
                ).show()
                NetworkResponse.Loading -> return@observe
                NetworkResponse.NetworkException -> Toast.makeText(context, "Please check your internet", Toast.LENGTH_SHORT)
                    .show()
                is NetworkResponse.Success -> {
                    val sharedPref = requireActivity().getSharedPreferences(Utility.USER_PREF, Context.MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        with(loginInformation.data.loginResult) {
                            putString(Utility.USERNAME, this.name)
                            putString(Utility.USER_ID, this.userId)
                            putString(Utility.USER_TOKEN, this.token)
                        }
                        apply()
                    }
                    view?.findNavController()?.navigate(R.id.login_to_main)
                }
                NetworkResponse.None -> return@observe
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.btnLogin.id -> checkIsValid(
                binding.edtEmail.text.toString(),
                binding.edtPassword.text.toString()
            )
            binding.tvRegister.id -> v.findNavController().navigate(R.id.login_to_register)
        }
    }

    private fun checkIsValid(email: String, password: String) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            binding.edtEmail.error = "Silahkan masukkan email yang valid"
        else
            viewModel.signInWithEmailAndPassword(email, password)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}