package com.example.tacticore.ui.build

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.tacticore.TacticoreApplication
import com.example.tacticore.data.HeroRepository
import com.example.tacticore.R
import com.example.tacticore.databinding.FragmentBuildListBinding
import kotlinx.coroutines.launch

class BuildListFragment : Fragment() {

    private var _binding: FragmentBuildListBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: HeroRepository
    private var heroId: Int = 0
    private var heroName: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBuildListBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        heroId = arguments?.getInt("heroId") ?: 0
        heroName = arguments?.getString("heroName") ?: ""

        if (heroId == 0) {
            Toast.makeText(requireContext(), "Грешка: липсва ID на герой", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
            return
        }

        binding.title.text = "Builds за $heroName"

        repository = (requireActivity().application as TacticoreApplication).repository

        binding.fabAddBuild.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("heroId", heroId)
                putString("heroName", heroName)
                putBoolean("forceNewBuild", true)
            }
            findNavController().navigate(R.id.heroDetailFragment, bundle)
        }

        loadBuilds()
    }

    private fun loadBuilds() {
        lifecycleScope.launch {
            val builds = repository.getAllBuildsForHero(heroId)
            if (builds.isEmpty()) {
                binding.recyclerViewBuilds.adapter = null
                Toast.makeText(
                    requireContext(),
                    "Няма запазени builds. Натиснете '+' за нов.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                val adapter = BuildListAdapter(
                    builds = builds,
                    onBuildEdit = { build ->
                        val bundle = Bundle().apply {
                            putInt("heroId", build.heroId)
                            putString("preloadMode", build.mode)
                            putString("preloadNotes", build.userNotes)
                            putInt("preloadRating", build.rating)
                            putString("preloadStadiumItems", build.stadiumItems ?: "")
                            putString("preloadStadiumGadgets", build.stadiumGadgets ?: "")
                            putString("preloadStadiumPower", build.stadiumPower ?: "")
                            putString("preloadQuickPlayPerks", build.quickPlayPerks ?: "")
                            putLong("preloadBuildId", build.id)
                        }
                        findNavController().navigate(R.id.heroDetailFragment, bundle)
                    },
                    onBuildDelete = { build ->
                        AlertDialog.Builder(requireContext())
                            .setTitle("Изтриване")
                            .setMessage("Сигурни ли сте, че искате да изтриете този build?")
                            .setPositiveButton("Да") { _, _ ->
                                lifecycleScope.launch {
                                    repository.deleteBuildById(build.id)
                                    Toast.makeText(
                                        requireContext(),
                                        "Build изтрит",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    loadBuilds() // презареждаме списъка
                                }
                            }
                            .setNegativeButton("Не", null)
                            .show()
                    }
                )
                binding.recyclerViewBuilds.adapter = adapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
