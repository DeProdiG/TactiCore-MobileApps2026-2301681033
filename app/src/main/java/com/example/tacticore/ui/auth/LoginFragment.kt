package com.example.tacticore.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tacticore.MainActivity
import com.example.tacticore.R
import com.example.tacticore.TacticoreApplication
import com.example.tacticore.data.AuthRepository
import com.example.tacticore.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var authRepo: AuthRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authRepo = AuthRepository(requireContext())

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Моля, попълнете всички полета", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userId = authRepo.login(username, password)
            if (userId != -1L) {
                authRepo.saveLoggedInUser(userId)
                (requireActivity().application as TacticoreApplication).refreshRepository()
                Toast.makeText(requireContext(), "Успешен вход", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_login_to_main)
            } else {
                Toast.makeText(requireContext(), "Грешно потребителско име или парола", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}