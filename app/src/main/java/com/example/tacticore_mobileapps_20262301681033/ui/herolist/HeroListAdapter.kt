package com.example.tacticore.ui.herolist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tacticore.R
import com.example.tacticore.data.Hero
import com.example.tacticore.databinding.ItemHeroBinding

class HeroListAdapter(
    private val onHeroClick: (Hero) -> Unit
) : RecyclerView.Adapter<HeroListAdapter.HeroViewHolder>() {

    private var heroes = listOf<Hero>()

    fun submitList(list: List<Hero>) {
        heroes = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {
        val binding = ItemHeroBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HeroViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
        val hero = heroes[position]
        holder.bind(hero)
        holder.itemView.setOnClickListener {
            it.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).withEndAction {
            it.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
            onHeroClick(hero)
        }.start() }
    }

    override fun getItemCount() = heroes.size

    inner class HeroViewHolder(private val binding: ItemHeroBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(hero: Hero) {
            binding.heroName.text = hero.name
            binding.heroRole.text = hero.role

            val context = binding.root.context
            val color = when (hero.role) {
                "Tank" -> ContextCompat.getColor(context, R.color.role_tank)      // #AD1D45
                "DPS" -> ContextCompat.getColor(context, R.color.role_dps)        // #F9D276
                "Support" -> ContextCompat.getColor(context, R.color.role_support)// #83142C
                else -> ContextCompat.getColor(context, R.color.role_unknown)     // #44000D
            }
            binding.heroRole.setBackgroundColor(color)
            if (hero.role == "DPS") {
                binding.heroRole.setTextColor(ContextCompat.getColor(context, R.color.black))
            } else {
                binding.heroRole.setTextColor(ContextCompat.getColor(context, R.color.white))
            }
            binding.heroImage.setImageResource(hero.imageResId)
        }
    }
}