package com.example.tacticore.ui.allbuilds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.tacticore.R
import com.example.tacticore.TacticoreApplication
import com.example.tacticore.data.HeroBuild
import com.example.tacticore.data.HeroRepository
import com.example.tacticore.ui.buildlist.BuildListAdapter
import kotlinx.coroutines.launch

class AllBuildsFragment : Fragment() {

    private lateinit var repository: HeroRepository
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_all_builds, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerViewAllBuilds)
        repository = (requireActivity().application as TacticoreApplication).repository

        loadAllBuilds()
    }

    private fun loadAllBuilds() {
        lifecycleScope.launch {
            val builds = repository.getAllBuilds()  // ще имплементираме този метод
            if (builds.isEmpty()) {
                Toast.makeText(requireContext(), "Няма създадени builds", Toast.LENGTH_SHORT).show()
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
                        // Изтриване с потвърждение
                        android.app.AlertDialog.Builder(requireContext())
                            .setTitle("Изтриване")
                            .setMessage("Сигурни ли сте?")
                            .setPositiveButton("Да") { _, _ ->
                                lifecycleScope.launch {
                                    repository.deleteBuildById(build.id)
                                    loadAllBuilds()
                                }
                            }
                            .setNegativeButton("Не", null)
                            .show()
                    }
                )
                recyclerView.adapter = adapter
            }
        }
    }
}