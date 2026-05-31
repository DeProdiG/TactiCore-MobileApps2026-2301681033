package com.example.tacticore.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tacticore.data.AuthRepository
import com.example.tacticore.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var authRepo: AuthRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authRepo = AuthRepository(requireContext())
        val userId = authRepo.getCurrentUserId()
        val username = authRepo.getUsername(userId)
        binding.tvUsername.text = username ?: "Не сте влезли в акаунта си."
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}