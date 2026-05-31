package com.example.tacticore_mobileapps_20262301681033.ui.counter

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.tacticore_mobileapps_20262301681033.R
import com.example.tacticore_mobileapps_20262301681033.TacticoreApplication
import com.example.tacticore_mobileapps_20262301681033.databinding.FragmentCounterBinding

class CounterFragment : Fragment() {

    private var _binding: FragmentCounterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCounterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repository = (requireActivity().application as TacticoreApplication).repository
        val heroes = repository.getHeroes().map { it.name }

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, heroes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerEnemy.adapter = adapter

        binding.btnFindCounter.setOnClickListener {
            val selectedEnemy = binding.spinnerEnemy.selectedItem.toString()
            val counters = CounterRules.getCountersForEnemy(selectedEnemy)

            binding.cardResult.apply {
                if (!isVisible) {
                    alpha = 0f
                    isVisible = true
                    animate().alpha(1f).setDuration(300).start()
                }
            }

            if (counters.isNotEmpty()) {
                val formatted = counters.joinToString("\n• ", "• ")
                binding.tvResult.text = formatted
            } else {
                binding.tvResult.text = "❌ Нямаме правила за $selectedEnemy още.\nНо можеш да пробваш с Winston, Reaper или Pharah!"
                binding.tvResult.setTextColor(
                    resources.getColor(android.R.color.holo_red_dark, null)
                )
            }
            // Анимация на бутона
            binding.btnFindCounter.animate().scaleX(0.98f).scaleY(0.98f).setDuration(100)
                .withEndAction {
                    binding.btnFindCounter.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
                }.start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
