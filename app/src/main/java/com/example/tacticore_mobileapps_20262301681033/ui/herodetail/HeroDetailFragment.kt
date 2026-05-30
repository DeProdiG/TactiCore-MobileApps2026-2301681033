package com.example.tacticore.ui.herodetail

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.tacticore.R
import com.example.tacticore.TacticoreApplication
import com.example.tacticore.databinding.FragmentHeroDetailBinding
import com.example.tacticore.data.HeroBuild
import com.example.tacticore.data.HeroRepository
import com.example.tacticore.ui.qr.QRGenerator
import kotlinx.coroutines.launch

class HeroDetailFragment : Fragment() {

    private var _binding: FragmentHeroDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: HeroRepository
    private var heroId: Int = 0
    private var heroName: String = ""
    private var currentMode = "stadium"
    private lateinit var currentModeFields: View
    private var currentBuildId: Long = 0
    private var isEditMode = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHeroDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        heroId = arguments?.getInt("heroId") ?: 0
        if (heroId == 0) {
            Toast.makeText(requireContext(), "Грешка: липсва ID на герой", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
            return
        }

        repository = (requireActivity().application as TacticoreApplication).repository
        val hero = repository.getHeroById(heroId)
        if (hero != null) {
            heroName = hero.name
            binding.heroName.text = hero.name
            binding.heroRole.text = hero.role
            binding.heroDescription.text = hero.description
            binding.heroImage.setImageResource(hero.imageResId)

            val color = when (hero.role) {
                "Tank" -> ContextCompat.getColor(requireContext(), R.color.role_tank)
                "DPS" -> ContextCompat.getColor(requireContext(), R.color.role_dps)
                "Support" -> ContextCompat.getColor(requireContext(), R.color.role_support)
                else -> ContextCompat.getColor(requireContext(), R.color.role_unknown)
            }
            binding.heroRole.setBackgroundColor(color)
            binding.heroRole.setPadding(24, 6, 24, 6)
        }

        val modes = resources.getStringArray(R.array.modes_array)
        val modeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, modes)
        modeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerMode.adapter = modeAdapter

        currentBuildId = arguments?.getLong("preloadBuildId") ?: 0L
        isEditMode = currentBuildId != 0L

        if (isEditMode) {
            val preloadMode = arguments?.getString("preloadMode") ?: "stadium"
            currentMode = preloadMode
            val modePosition = if (preloadMode == "stadium") 0 else 1
            binding.spinnerMode.setSelection(modePosition)
            binding.spinnerMode.isEnabled = false

            binding.etNotes.setText(arguments?.getString("preloadNotes") ?: "")
            binding.ratingBar.rating = (arguments?.getInt("preloadRating") ?: 3).toFloat()

            switchModeFields()

            when (currentMode) {
                "stadium" -> {
                    currentModeFields.findViewById<EditText>(R.id.etStadiumItems)?.setText(arguments?.getString("preloadStadiumItems") ?: "")
                    currentModeFields.findViewById<EditText>(R.id.etStadiumGadgets)?.setText(arguments?.getString("preloadStadiumGadgets") ?: "")
                    currentModeFields.findViewById<EditText>(R.id.etStadiumPower)?.setText(arguments?.getString("preloadStadiumPower") ?: "")
                }
                "quickplay" -> {
                    currentModeFields.findViewById<EditText>(R.id.etQuickPlayPerks)?.setText(arguments?.getString("preloadQuickPlayPerks") ?: "")
                }
            }

            binding.btnSave.text = "Запази промени"
            binding.btnDelete.text = "Откажи"
            binding.btnDelete.setOnClickListener {
                navigateToBuildList()
            }
            binding.btnSave.setOnClickListener {
                updateCurrentBuild()
            }
        } else {
            binding.spinnerMode.isEnabled = true
            binding.btnSave.text = "Запази"
            binding.btnDelete.text = "Изтрий"
            binding.btnSave.setOnClickListener { saveNewBuild() }
            binding.btnDelete.setOnClickListener { deleteCurrentBuild() }
            binding.spinnerMode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    currentMode = if (position == 0) "stadium" else "quickplay"
                    switchModeFields()
                    loadBuildForCurrentMode()
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
            switchModeFields()
            loadBuildForCurrentMode()
        }

        binding.btnShowAllBuilds.setOnClickListener {
            navigateToBuildList()
        }

        binding.btnShareQR.setOnClickListener {
            val heroName = hero?.name ?: return@setOnClickListener
            val notes = binding.etNotes.text.toString()
            val rating = binding.ratingBar.rating.toInt()
            val modeText = if (currentMode == "stadium") "Stadium" else "QuickPlay"
            val extraData = when (currentMode) {
                "stadium" -> {
                    val items = currentModeFields.findViewById<EditText>(R.id.etStadiumItems).text.toString()
                    val gadgets = currentModeFields.findViewById<EditText>(R.id.etStadiumGadgets).text.toString()
                    val power = currentModeFields.findViewById<EditText>(R.id.etStadiumPower).text.toString()
                    "|Items:$items|Gadgets:$gadgets|Power:$power"
                }
                else -> {
                    val perks = currentModeFields.findViewById<EditText>(R.id.etQuickPlayPerks).text.toString()
                    "|Perks:$perks"
                }
            }
            val qrData = "$heroName|$modeText|$notes|$rating$extraData"
            QRGenerator.showQRCodeDialog(requireContext(), qrData)
        }
    }

    private fun updateCurrentBuild() {
        val notes = binding.etNotes.text.toString()
        val rating = binding.ratingBar.rating.toInt()
        val build = HeroBuild(
            id = currentBuildId,
            heroId = heroId,
            mode = currentMode,
            userNotes = notes,
            rating = rating,
            stadiumItems = if (currentMode == "stadium") currentModeFields.findViewById<EditText>(R.id.etStadiumItems).text.toString() else null,
            stadiumGadgets = if (currentMode == "stadium") currentModeFields.findViewById<EditText>(R.id.etStadiumGadgets).text.toString() else null,
            stadiumPower = if (currentMode == "stadium") currentModeFields.findViewById<EditText>(R.id.etStadiumPower).text.toString() else null,
            quickPlayPerks = if (currentMode == "quickplay") currentModeFields.findViewById<EditText>(R.id.etQuickPlayPerks).text.toString() else null
        )
        lifecycleScope.launch {
            repository.saveBuild(build)
            Toast.makeText(requireContext(), "Промените са запазени", Toast.LENGTH_SHORT).show()
            navigateToBuildList()
        }
    }

    private fun saveNewBuild() {
        val notes = binding.etNotes.text.toString()
        val rating = binding.ratingBar.rating.toInt()
        val build = HeroBuild(
            heroId = heroId,
            mode = currentMode,
            userNotes = notes,
            rating = rating,
            stadiumItems = if (currentMode == "stadium") currentModeFields.findViewById<EditText>(R.id.etStadiumItems).text.toString() else null,
            stadiumGadgets = if (currentMode == "stadium") currentModeFields.findViewById<EditText>(R.id.etStadiumGadgets).text.toString() else null,
            stadiumPower = if (currentMode == "stadium") currentModeFields.findViewById<EditText>(R.id.etStadiumPower).text.toString() else null,
            quickPlayPerks = if (currentMode == "quickplay") currentModeFields.findViewById<EditText>(R.id.etQuickPlayPerks).text.toString() else null
        )
        lifecycleScope.launch {
            repository.saveBuild(build)
            Toast.makeText(requireContext(), "Новият build е запазен", Toast.LENGTH_SHORT).show()
            navigateToBuildList()
        }
    }

    private fun deleteCurrentBuild() {
        AlertDialog.Builder(requireContext())
            .setTitle("Изтриване")
            .setMessage("Сигурни ли сте, че искате да изтриете този build?")
            .setPositiveButton("Да") { _, _ ->
                lifecycleScope.launch {
                    repository.deleteBuild(heroId, currentMode)
                    clearModeFields()
                    Toast.makeText(requireContext(), "Билдът е изтрит", Toast.LENGTH_SHORT).show()
                    navigateToBuildList()
                }
            }
            .setNegativeButton("Не", null)
            .show()
    }

    private fun switchModeFields() {
        val inflater = LayoutInflater.from(requireContext())
        currentModeFields = when (currentMode) {
            "stadium" -> inflater.inflate(R.layout.stadium_fields, binding.modeSpecificContainer, false)
            else -> inflater.inflate(R.layout.quickplay_fields, binding.modeSpecificContainer, false)
        }
        binding.modeSpecificContainer.removeAllViews()
        binding.modeSpecificContainer.addView(currentModeFields)
    }

    private fun loadBuildForCurrentMode() {
        lifecycleScope.launch {
            val build = repository.getBuildForHero(heroId, currentMode)
            updateUIWithBuild(build)
        }
    }

    private fun updateUIWithBuild(build: HeroBuild?) {
        if (build != null) {
            currentBuildId = build.id
            binding.etNotes.setText(build.userNotes)
            binding.ratingBar.rating = build.rating.toFloat()
            when (currentMode) {
                "stadium" -> {
                    currentModeFields.findViewById<EditText>(R.id.etStadiumItems)?.setText(build.stadiumItems ?: "")
                    currentModeFields.findViewById<EditText>(R.id.etStadiumGadgets)?.setText(build.stadiumGadgets ?: "")
                    currentModeFields.findViewById<EditText>(R.id.etStadiumPower)?.setText(build.stadiumPower ?: "")
                }
                "quickplay" -> {
                    currentModeFields.findViewById<EditText>(R.id.etQuickPlayPerks)?.setText(build.quickPlayPerks ?: "")
                }
            }
        } else {
            clearModeFields()
            currentBuildId = 0
        }
    }

    private fun clearModeFields() {
        binding.etNotes.setText("")
        binding.ratingBar.rating = 3f
        when (currentMode) {
            "stadium" -> {
                currentModeFields.findViewById<EditText>(R.id.etStadiumItems)?.setText("")
                currentModeFields.findViewById<EditText>(R.id.etStadiumGadgets)?.setText("")
                currentModeFields.findViewById<EditText>(R.id.etStadiumPower)?.setText("")
            }
            "quickplay" -> {
                currentModeFields.findViewById<EditText>(R.id.etQuickPlayPerks)?.setText("")
            }
        }
    }

    private fun navigateToBuildList() {
        val bundle = Bundle().apply {
            putInt("heroId", heroId)
            putString("heroName", heroName)
        }
        findNavController().navigate(R.id.buildListFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}