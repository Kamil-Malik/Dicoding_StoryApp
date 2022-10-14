package com.lelestacia.dicodingstoryapp.ui.register_activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.lelestacia.dicodingstoryapp.R
import com.lelestacia.dicodingstoryapp.databinding.ActivityRegisterBinding
import com.lelestacia.dicodingstoryapp.utility.NetworkResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        subscribe()
        binding.apply {
            tvLogin.setOnClickListener(this@RegisterActivity)
            btnRegister.setOnClickListener(this@RegisterActivity)
        }
    }

    private fun subscribe() {
        viewModel.registerInformation.observe(this) { registerStatus ->
            when (registerStatus) {
                is NetworkResponse.GenericException -> Toast.makeText(
                    this,
                    resources.getString(
                        R.string.error_message,
                        registerStatus.code,
                        registerStatus.cause
                    ),
                    Toast.LENGTH_SHORT
                ).show()
                NetworkResponse.NetworkException -> Toast.makeText(
                    this,
                    resources.getString(R.string.error_network),
                    Toast.LENGTH_SHORT
                ).show()
                is NetworkResponse.Success -> Toast.makeText(
                    this,
                    registerStatus.data.message,
                    Toast.LENGTH_SHORT
                ).show()
                else -> return@observe
            }
        }

        viewModel.registerInformation.observe(this) {
            when (it) {
                NetworkResponse.Loading -> binding.apply {
                    (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                        .hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                    layoutLoading.root.visibility = View.VISIBLE
                    btnRegister.visibility = View.GONE
                }
                else -> binding.apply {
                    layoutLoading.root.visibility = View.GONE
                    btnRegister.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun registerAccount() {
        val username = binding.edtUsername.text.toString()
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()
        if (binding.edtEmail.error.isEmpty() && binding.edtPassword.error.isEmpty())
            viewModel.signUpWithEmailAndPassword(username, email, password)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.tvLogin.id -> onBackPressedDispatcher.onBackPressed()
            binding.btnRegister.id -> registerAccount()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}