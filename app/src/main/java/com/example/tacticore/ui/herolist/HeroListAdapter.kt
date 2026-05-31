package com.example.tacticore.ui.herolist

import android.annotation.SuppressLint
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

    @SuppressLint("NotifyDataSetChanged")
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
        holder.itemView.setOnClickListener { onHeroClick(hero) }
    }

    override fun getItemCount() = heroes.size

    class HeroViewHolder(private val binding: ItemHeroBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(hero: Hero) {
            binding.heroName.text = hero.name
            binding.heroRole.text = hero.role

            val context = binding.root.context
            val color = when (hero.role) {
                "Tank" -> ContextCompat.getColor(context, R.color.role_tank)
                "DPS" -> ContextCompat.getColor(context, R.color.role_dps)
                "Support" -> ContextCompat.getColor(context, R.color.role_support)
                else -> ContextCompat.getColor(context, R.color.role_unknown)
            }
            val drawable = ContextCompat.getDrawable(context, R.drawable.bg_role_badge)?.mutate()
            drawable?.setTint(color)
            binding.heroRole.background = drawable

            if (hero.role == "DPS") {
                binding.heroRole.setTextColor(ContextCompat.getColor(context, R.color.black))
            } else {
                binding.heroRole.setTextColor(ContextCompat.getColor(context, R.color.white))
            }

            binding.heroImage.setImageResource(hero.imageResId)
        }
    }
}
