package com.lelestacia.dicodingstoryapp.ui.login_activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.lelestacia.dicodingstoryapp.R
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

        viewModel.loginInfo.observe(this) {
            when (it) {
                is NetworkResponse.Loading -> {
                    (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                        .hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                    binding.layoutLoading.root.visibility = View.VISIBLE
                    binding.btnLogin.visibility = View.GONE
                }
                is NetworkResponse.Success -> return@observe
                else -> {
                    binding.btnLogin.visibility = View.VISIBLE
                    binding.layoutLoading.root.visibility = View.GONE
                }
            }
        }

        viewModel.loginInfo.observe(this) { login ->
            when (login) {
                is NetworkResponse.GenericException -> Toast.makeText(
                    this,
                    resources.getString(R.string.error_message, login.code, login.cause),
                    Toast.LENGTH_SHORT
                ).show()
                is NetworkResponse.NetworkException -> Toast.makeText(
                    this,
                    resources.getString(R.string.error_network),
                    Toast.LENGTH_SHORT
                ).show()
                is NetworkResponse.Success -> startActivity(Intent(this, StoriesActivity::class.java))
                    .also { finish() }
                else -> return@observe
            }
        }

        binding.apply {
            btnLogin.setOnClickListener(this@LoginActivity)
            btnLogin.isEnabled = false
            tvRegister.setOnClickListener(this@LoginActivity)
            edtPassword.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    return
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    when {
                        (s?.length ?: 0) >= 6 -> btnLogin.isEnabled = true
                        else -> {
                            btnLogin.isEnabled = false
                            edtPassword.error = resources.getString(R.string.empty_password)
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    return
                }

            })
        }
    }

    private fun signInWithEmailAndPassword(email: String, password: String) {
        when {
            email.isEmpty() -> binding.edtEmail.error = resources.getString(R.string.empty_email)
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> binding.edtEmail.error = resources.getString(R.string.invalid_email)
            else -> viewModel.signInWithEmailAndPassword(email, password)
        }
    }

    override fun onClick(v: View?) {
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()

        when (v?.id) {
            binding.btnLogin.id -> signInWithEmailAndPassword(email, password)
            binding.tvRegister.id -> startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}