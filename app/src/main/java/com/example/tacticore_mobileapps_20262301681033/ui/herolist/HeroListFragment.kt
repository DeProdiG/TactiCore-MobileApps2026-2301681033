package com.example.tacticore.ui.herolist

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tacticore.R
import com.example.tacticore.TacticoreApplication
import com.example.tacticore.data.Hero
import com.example.tacticore.data.HeroRepository
import com.example.tacticore.databinding.FragmentHeroListBinding
import com.example.tacticore.ui.qr.QRScannerActivity

class HeroListFragment : Fragment() {

    private var _binding: FragmentHeroListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: HeroListAdapter
    private lateinit var repository: HeroRepository
    private var allHeroes = listOf<Hero>()
    private var currentQuery = ""
    private var currentRole = "Всички"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHeroListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireActivity().application as TacticoreApplication).repository
        allHeroes = repository.getHeroes()

        if (allHeroes.isEmpty()) {
            Toast.makeText(requireContext(), "Грешка: Няма заредени герои!", Toast.LENGTH_LONG).show()
            return
        }

        adapter = HeroListAdapter { hero ->
            val bundle = Bundle().apply { putInt("heroId", hero.id) }
            findNavController().navigate(R.id.heroDetailFragment, bundle)
        }
        binding.recyclerViewHeroes.adapter = adapter
        applyFilters()

        binding.btnCounterHelp.setOnClickListener {
            findNavController().navigate(R.id.counterFragment)
        }

        val roles = listOf("Всички", "DPS", "Tank", "Support")
        val roleAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, roles)
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerRoleFilter.adapter = roleAdapter
        binding.spinnerRoleFilter.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: View?, position: Int, id: Long) {
                currentRole = roles[position]
                applyFilters()
            }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }

        binding.etSearch.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                currentQuery = s.toString().lowercase()
                applyFilters()
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })

        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH) {
                val imm = requireContext().getSystemService(android.app.Activity.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
                imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
                true
            } else false
        }

        binding.recyclerViewHeroes.alpha = 0f
        binding.recyclerViewHeroes.animate().alpha(1f).setDuration(300).start()

        binding.fabQrScan.setOnClickListener {
            startActivity(Intent(requireContext(), QRScannerActivity::class.java))
        }
    }

    private fun applyFilters() {
        val filtered = allHeroes.filter { hero ->
            (currentRole == "Всички" || hero.role.equals(currentRole, ignoreCase = true)) &&
                    (currentQuery.isEmpty() || hero.name.lowercase().contains(currentQuery))
        }
        adapter.submitList(filtered)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}