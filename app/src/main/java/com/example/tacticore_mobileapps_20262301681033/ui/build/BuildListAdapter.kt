package com.example.tacticore.ui.buildlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tacticore.R
import com.example.tacticore.data.HeroBuild
import java.text.SimpleDateFormat
import java.util.*

class BuildListAdapter(
    private val builds: List<HeroBuild>,
    private val onBuildEdit: (HeroBuild) -> Unit,
    private val onBuildDelete: (HeroBuild) -> Unit
) : RecyclerView.Adapter<BuildListAdapter.BuildViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuildViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_build, parent, false)
        return BuildViewHolder(view)
    }

    override fun onBindViewHolder(holder: BuildViewHolder, position: Int) {
        val build = builds[position]
        holder.bind(build)
        holder.itemView.setOnClickListener { onBuildEdit(build) }
        holder.btnEditBuild.setOnClickListener { onBuildEdit(build) }
        holder.btnDeleteBuild.setOnClickListener { onBuildDelete(build) }
    }

    override fun getItemCount() = builds.size

    inner class BuildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val modeText: TextView = itemView.findViewById(R.id.tvMode)
        val notesPreview: TextView = itemView.findViewById(R.id.tvNotesPreview)
        val ratingText: TextView = itemView.findViewById(R.id.tvRating)
        val dateText: TextView = itemView.findViewById(R.id.tvDate)
        val btnEditBuild: ImageButton = itemView.findViewById(R.id.btnEditBuild)
        val btnDeleteBuild: ImageButton = itemView.findViewById(R.id.btnDeleteBuild)

        fun bind(build: HeroBuild) {
            val mode = if (build.mode == "stadium") "🏟️ Stadium" else "⚡ Quick Play"
            modeText.text = mode
            notesPreview.text = build.userNotes.take(50) + if (build.userNotes.length > 50) "..." else ""
            ratingText.text = "⭐ ${build.rating}/5"
            val date = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(build.timestamp))
            dateText.text = date
        }
    }
}