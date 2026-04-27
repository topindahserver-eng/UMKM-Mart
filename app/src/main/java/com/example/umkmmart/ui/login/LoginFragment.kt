package com.example.umkmmart.ui.login

import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.umkmmart.R
import com.example.umkmmart.data.pref.SessionManager
import com.example.umkmmart.databinding.FragmentLoginBinding

class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)
        sessionManager = SessionManager(requireContext())
        
        // CEK SESSION: Jika sudah login, langsung lompat ke Katalog
        if (sessionManager.isLoggedIn()) {
            findNavController().navigate(R.id.action_loginFragment_to_catalogFragment)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (validate(email, password)) {
                // SIMPAN SESSION sebelum pindah halaman
                sessionManager.saveLoginSession(email)
                findNavController().navigate(R.id.action_loginFragment_to_catalogFragment)
            }
        }
    }

    private fun validate(email: String, pass: String): Boolean {
        var isValid = true
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = "Email tidak valid"
            isValid = false
        } else {
            binding.tilEmail.error = null
        }
        if (pass.length < 6) {
            binding.tilPassword.error = "Password minimal 6 karakter"
            isValid = false
        } else {
            binding.tilPassword.error = null
        }
        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}