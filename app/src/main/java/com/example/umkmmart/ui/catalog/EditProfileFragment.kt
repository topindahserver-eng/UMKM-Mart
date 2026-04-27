package com.example.umkmmart.ui.catalog

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.umkmmart.R
import com.example.umkmmart.data.pref.SessionManager
import com.example.umkmmart.databinding.FragmentEditProfileBinding

class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEditProfileBinding.bind(view)
        sessionManager = SessionManager(requireContext())

        binding.toolbarEditProfile.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnSaveProfile.setOnClickListener {
            val newName = binding.etEditName.text.toString().trim()
            val newEmail = binding.etEditEmail.text.toString().trim()

            if (newName.isNotEmpty() && newEmail.isNotEmpty()) {
                // Dalam demo ini kita simpan ke SessionManager saja
                sessionManager.saveLoginSession(newEmail)
                Toast.makeText(context, "Profil diperbarui!", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            } else {
                Toast.makeText(context, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}