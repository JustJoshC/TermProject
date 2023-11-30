package com.sheridancollege.cowanjos.advandtermproj

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sheridancollege.cowanjos.advandtermproj.databinding.CyclingItemBinding
import com.sheridancollege.cowanjos.advandtermproj.databinding.FreeWeightsItemBinding
import kotlinx.coroutines.flow.Flow

class CyclingAdapter(
    private val onEditClicked: (Cycling) -> Unit,
    private val onDeleteClicked: (Cycling) -> Unit
) : ListAdapter<Cycling, CyclingAdapter.CyclingViewHolder>(DiffCallBack) {
    private var cyclingList: List<Cycling> = emptyList()
    class CyclingViewHolder(val binding: CyclingItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CyclingViewHolder {
        return CyclingViewHolder(
            CyclingItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: CyclingViewHolder, position: Int) {
        val current = cyclingList[position]
        holder.binding.apply {
            distanceTextView.text = current.rideDistance + " Km"
            durationTextView.text = current.rideDuration
            dateTextView.text = current.date.toString()

            editButton.setOnClickListener{
                onEditClicked(current)
            }
            deleteButton.setOnClickListener {
                onDeleteClicked(current)
            }
        }
    }

    override fun getItemCount(): Int {
        return cyclingList.size
    }
    fun setCycling(cycling: List<Cycling>) {
        this.cyclingList = cycling
        notifyDataSetChanged()
    }
    companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<Cycling>() {

            override fun areItemsTheSame(oldItem: Cycling, newItem: Cycling): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Cycling, newItem: Cycling): Boolean {
                return oldItem.cyclingId == newItem.cyclingId
            }
        }
    }

}