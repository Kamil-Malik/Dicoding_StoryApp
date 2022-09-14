package com.lelestacia.dicodingstoryapp.ui.login_activity

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.lelestacia.dicodingstoryapp.databinding.ActivityLoginBinding
import com.lelestacia.dicodingstoryapp.ui.register_activity.RegisterActivity
import com.lelestacia.dicodingstoryapp.ui.stories_activity.StoriesActivity
import com.lelestacia.dicodingstoryapp.utility.NetworkResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        viewModel.loginInfo.observe(this) { login ->
            when (login) {
                is NetworkResponse.GenericException -> Toast.makeText(
                    this, "Error ${login.code} " +
                            ": ${login.cause}", Toast.LENGTH_SHORT
                )
                    .show()
                NetworkResponse.Loading -> return@observe
                NetworkResponse.NetworkException -> Toast.makeText(
                    this,
                    "Silahkan periksa koneksi anda",
                    Toast.LENGTH_SHORT
                )
                    .show()
                NetworkResponse.None -> return@observe
                is NetworkResponse.Success -> if (!login.data.error)
                    startActivity(Intent(this, StoriesActivity::class.java))
            }
        }
        binding.btnLogin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()

        when (v?.id) {
            binding.btnLogin.id -> checkIsValid(email, password)
            binding.tvRegister.id -> startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun checkIsValid(email: String, password: String) {
        if (email.isEmpty())
            binding.edtEmail.error = "Email tidak boleh kosong"
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            binding.edtEmail.error = "Silahkan masukkan email yang valid"
        if (password.isEmpty() || password.length < 6)
            binding.edtPassword.error = "Password tidak boleh kosong"
        else
            viewModel.signInWithEmailAndPassword(email, password)
    }
}