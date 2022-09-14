package com.lelestacia.dicodingstoryapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.lelestacia.dicodingstoryapp.databinding.FragmentRegisterBinding
import com.lelestacia.dicodingstoryapp.ui.viewmodel.MainViewModel

class RegisterFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewmodel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.tvLogin.setOnClickListener(this)
        binding.btnRegister.setOnClickListener(this)
        return binding.root
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.tvLogin.id -> activity?.onBackPressed()
            binding.btnRegister.id -> registerAccount()
        }
    }

    private fun registerAccount() {
        val username = binding.edtUsername.text.toString()
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()

        if (username.isEmpty())
            binding.edtUsername.error = "Username tidak boleh kosong"
        if (email.isEmpty())
            binding.edtEmail.error = "Email tidak boleh kosong"
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
            binding.edtEmail.error = "Email tidak valid"
        if (password.length < 6 || password.isEmpty())
            binding.edtPassword.error = "Password tidak boleh kosong atau kurang dari 6 karakter"
        if (username.isNotEmpty() && email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS
                .matcher(email).matches() && password.length < 6)
            viewmodel.signUpWithEmailAndPassword(username,email, password)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}