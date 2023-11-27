package com.sheridancollege.cowanjos.advandtermproj

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sheridancollege.cowanjos.advandtermproj.databinding.FreeWeightsItemBinding

class FreeWeightsAdapter(
    private val onDeleteClicked: (FreeWeights) -> Unit // Callback function for delete action
) : RecyclerView.Adapter<FreeWeightsAdapter.FreeWeightsViewHolder>() {

    private var freeWeightsList: List<FreeWeights> = emptyList()

    // ViewHolder class to hold the item view
    class FreeWeightsViewHolder(val binding: FreeWeightsItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FreeWeightsViewHolder {
        val binding = FreeWeightsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FreeWeightsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FreeWeightsViewHolder, position: Int) {
        val currentItem = freeWeightsList[position]
        holder.binding.apply {
            muscleGroupTextView.text = currentItem.muscleGroup
            durationTextView.text = currentItem.workoutDuration
            dateTextView.text = currentItem.date.toString()

            // Set the delete button listener
            deleteButton.setOnClickListener {
                onDeleteClicked(currentItem) // Invoke the callback with the current item
            }
        }
    }

    override fun getItemCount() = freeWeightsList.size

    // Method to update the data in the adapter
    fun setFreeWeights(freeWeights: List<FreeWeights>) {
        this.freeWeightsList = freeWeights
        notifyDataSetChanged()
    }
}
