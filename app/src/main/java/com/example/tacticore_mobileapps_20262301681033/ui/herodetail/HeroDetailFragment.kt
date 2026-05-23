package com.example.tacticore.ui.herodetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tacticore.TacticoreApplication
import com.example.tacticore.databinding.FragmentHeroDetailBinding
import com.example.tacticore.ui.qr.QRGenerator

class HeroDetailFragment : Fragment() {

    private var _binding: FragmentHeroDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HeroDetailViewModel
    private var heroId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHeroDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        heroId = arguments?.getInt("heroId") ?: -1
        
        val repository = (requireActivity().application as TacticoreApplication).repository
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return HeroDetailViewModel(repository) as T
            }
        })[HeroDetailViewModel::class.java]

        viewModel.loadHero(heroId)

        viewModel.hero.observe(viewLifecycleOwner) { hero ->
            hero?.let {
                binding.heroName.text = it.name
                binding.heroRole.text = it.role
                binding.heroDescription.text = it.description
                binding.heroImage.setImageResource(it.imageResId)
            }
        }

        viewModel.build.observe(viewLifecycleOwner) { build ->
            if (build != null) {
                binding.etNotes.setText(build.userNotes)
                binding.ratingBar.rating = build.rating.toFloat()
            } else {
                binding.etNotes.setText("")
                binding.ratingBar.rating = 0f
            }
        }

        binding.btnSave.setOnClickListener {
            val notes = binding.etNotes.text.toString()
            val rating = binding.ratingBar.rating.toInt()
            viewModel.saveBuild(heroId, notes, rating)
            Toast.makeText(requireContext(), "Промените са запазени", Toast.LENGTH_SHORT).show()
        }

        binding.btnDelete.setOnClickListener {
            viewModel.deleteBuild(heroId)
            Toast.makeText(requireContext(), "Билдът е изтрит", Toast.LENGTH_SHORT).show()
        }

        binding.btnShareQR.setOnClickListener {
            val heroName = viewModel.hero.value?.name ?: "Unknown"
            val notes = binding.etNotes.text.toString()
            val rating = binding.ratingBar.rating.toInt()
            
            val qrData = "$heroName|$notes|$rating"
            QRGenerator.showQRCodeDialog(requireContext(), qrData)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}