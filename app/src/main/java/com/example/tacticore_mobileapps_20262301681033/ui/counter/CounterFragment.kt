package com.example.tacticore.ui.counter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.tacticore.TacticoreApplication
import com.example.tacticore.databinding.FragmentCounterBinding

class CounterFragment : Fragment() {

    private var _binding: FragmentCounterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCounterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = (requireActivity().application as TacticoreApplication).repository
        val heroes = repository.getHeroes()
        val heroNames = heroes.map { it.name }

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, heroNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerEnemy.adapter = adapter

        binding.btnFindCounter.setOnClickListener {
            val selectedHeroName = binding.spinnerEnemy.selectedItem?.toString()
            val selectedHero = heroes.find { it.name == selectedHeroName }
            
            val counterText = when (selectedHero?.role) {
                "Tank" -> "Срещу Tank (като ${selectedHero.name}) пробвайте DPS с висок Damage."
                "DPS" -> "Срещу DPS (като ${selectedHero.name}) пробвайте Tank с щит или мобилен DPS."
                "Support" -> "Срещу Support (като ${selectedHero.name}) пробвайте флангови герои като Tracer."
                else -> "Изберете вражески герой, за да видите съвет."
            }
            binding.tvResult.text = counterText
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}