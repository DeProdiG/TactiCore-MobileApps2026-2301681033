package com.example.tacticore.ui.herolist

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.tacticore.R
import com.example.tacticore.TacticoreApplication
import com.example.tacticore.databinding.FragmentHeroListBinding
import com.example.tacticore.ui.herolist.HeroListViewModel
import com.example.tacticore.ui.herolist.HeroListAdapter
import com.example.tacticore.ui.qr.QRScannerActivity

class HeroListFragment : Fragment() {

    private var _binding: FragmentHeroListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HeroListViewModel
    private lateinit var adapter: HeroListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHeroListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repository = (requireActivity().application as TacticoreApplication).repository
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return HeroListViewModel(repository) as T
            }
        })[HeroListViewModel::class.java]

        adapter = HeroListAdapter { hero ->
            val bundle = Bundle().apply {
                putInt("heroId", hero.id)
            }
            findNavController().navigate(R.id.action_heroList_to_heroDetail, bundle)
        }
        binding.recyclerViewHeroes.adapter = adapter

        viewModel.heroes.observe(viewLifecycleOwner) { heroes ->
            adapter.submitList(heroes)
        }

        setHasOptionsMenu(true)

        binding.fabQrScan.setOnClickListener {
            startActivity(Intent(requireContext(), QRScannerActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_counter_help) {
            findNavController().navigate(R.id.action_heroList_to_counterFragment)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}