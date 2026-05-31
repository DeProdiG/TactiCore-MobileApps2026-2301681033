package com.example.tacticore.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tacticore.R
import com.example.tacticore.data.AuthRepository
import com.example.tacticore.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var authRepo: AuthRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authRepo = AuthRepository(requireContext())

        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString()
            val confirm = binding.etConfirmPassword.text.toString()

            if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(requireContext(), "Моля, попълнете всички полета", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirm) {
                Toast.makeText(requireContext(), "Паролите не съвпадат", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val success = authRepo.register(username, password)
            if (success) {
                Toast.makeText(requireContext(), "Регистрацията е успешна. Впишете се.", Toast.LENGTH_SHORT).show()
                // Връщаме се към LoginFragment
                findNavController().navigateUp()
            } else {
                Toast.makeText(requireContext(), "Потребителското име вече съществува", Toast.LENGTH_SHORT).show()
            }
        }

        // Добавяме действие при натискане на текста за вход
        binding.tvGoToLogin.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}