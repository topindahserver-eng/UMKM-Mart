package com.example.umkmmart.ui.login

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.umkmmart.R
import com.example.umkmmart.data.local.AppDatabase
import com.example.umkmmart.data.local.User
import com.example.umkmmart.data.pref.SessionManager
import com.example.umkmmart.databinding.FragmentRegisterBinding
import kotlinx.coroutines.launch

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRegisterBinding.bind(view)
        sessionManager = SessionManager(requireContext())

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnRegister.setOnClickListener {
            val name = binding.etFullName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (validate(name, email, password)) {
                val db = AppDatabase.getDatabase(requireContext())
                val newUser = User(email, name, password)

                lifecycleScope.launch {
                    val existingUser = db.userDao().getUserByEmail(email)
                    if (existingUser == null) {
                        db.userDao().insertUser(newUser)
                        sessionManager.saveLoginSession(email)
                        Toast.makeText(context, "Akun Berhasil Dibuat!", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_registerFragment_to_catalogFragment)
                    } else {
                        binding.tilEmail.error = "Email sudah terdaftar"
                    }
                }
            }
        }

        binding.tvToLogin.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun validate(name: String, email: String, pass: String): Boolean {
        var isValid = true
        if (name.isEmpty()) {
            binding.tilFullName.error = "Nama wajib diisi"
            isValid = false
        } else {
            binding.tilFullName.error = null
        }
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