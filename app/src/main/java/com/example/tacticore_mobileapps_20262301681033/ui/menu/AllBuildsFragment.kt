package com.example.tacticore.ui.allbuilds

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
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
    private lateinit var heroNames: List<String>
    private lateinit var heroIds: List<Int>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_all_builds, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerViewAllBuilds)
        val fabAddBuild = view.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fabAddBuild)

        repository = (requireActivity().application as TacticoreApplication).repository

        val heroes = repository.getHeroes()
        heroNames = heroes.map { it.name }
        heroIds = heroes.map { it.id }

        fabAddBuild.setOnClickListener {
            showHeroSelectionDialog()
        }

        loadAllBuilds()
    }

    private fun showHeroSelectionDialog() {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, heroNames)
        AlertDialog.Builder(requireContext())
            .setTitle("Избери герой за нов build")
            .setAdapter(adapter) { _, which ->
                val heroId = heroIds[which]
                val heroName = heroNames[which]
                val bundle = Bundle().apply {
                    putInt("heroId", heroId)
                    putString("heroName", heroName)
                }
                findNavController().navigate(R.id.heroDetailFragment, bundle)
            }
            .setNegativeButton("Отказ", null)
            .show()
    }

    private fun loadAllBuilds() {
        lifecycleScope.launch {
            val builds = repository.getAllBuilds()
            if (builds.isEmpty()) {
                recyclerView.adapter = null
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
                        AlertDialog.Builder(requireContext())
                            .setTitle("Изтриване")
                            .setMessage("Сигурни ли сте?")
                            .setPositiveButton("Да") { _, _ ->
                                lifecycleScope.launch {
                                    repository.deleteBuildById(build.id)
                                    loadAllBuilds() // презареждаме
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