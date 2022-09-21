package com.lelestacia.dicodingstoryapp.ui.register_activity

 import android.os.Bundle
 import android.text.Editable
 import android.text.TextWatcher
 import android.util.Patterns
 import android.view.View
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
            btnRegister.isEnabled = false

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
                        (s?.length ?: 0) >= 6 -> btnRegister.isEnabled = true
                        else -> {
                            btnRegister.isEnabled = false
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

        when {
            username.isEmpty() ->  binding.edtUsername.error = resources.getString(R.string.empty_username)
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                binding.edtEmail.error = resources.getString(R.string.invalid_email)
            else -> viewModel.signUpWithEmailAndPassword(username, email, password)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.tvLogin.id -> onBackPressed()
            binding.btnRegister.id -> registerAccount()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}