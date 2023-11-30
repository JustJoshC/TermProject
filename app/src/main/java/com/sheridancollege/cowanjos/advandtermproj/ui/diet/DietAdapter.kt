package com.sheridancollege.cowanjos.advandtermproj.ui.diet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sheridancollege.cowanjos.advandtermproj.Diet
import com.sheridancollege.cowanjos.advandtermproj.FreeWeights
import com.sheridancollege.cowanjos.advandtermproj.Meal
import com.sheridancollege.cowanjos.advandtermproj.databinding.MealTableItemBinding

class DietAdapter () : RecyclerView.Adapter<DietAdapter.DietViewHolder>(){

    private var mealItemList: List<Meal> = emptyList()

    class DietViewHolder(val binding: MealTableItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DietAdapter.DietViewHolder {
        val binding = MealTableItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DietAdapter.DietViewHolder(binding)
    }

    override fun getItemCount() = mealItemList.size

    override fun onBindViewHolder(holder: DietViewHolder, position: Int) {
        val currentItem = mealItemList[position]
        holder.binding.apply {
            textMealName.text = currentItem.mealDescription
            textMealCalories.text = currentItem.mealCalories.toString()
            textMealDate.text = currentItem.mealTime
        }
    }

    fun setMeals(addMeal: List<Meal>) {
        this.mealItemList = addMeal
        notifyDataSetChanged()
    }

    fun calculateCalories(): Int {
        var totalCalories = 0

        // Calculate the starting index for the last three items
        val startIndex = if (mealItemList.size >= 3) mealItemList.size - 3 else 0

        for (i in startIndex until mealItemList.size) {
            val meal = mealItemList[i]
            totalCalories += meal.mealCalories // Assuming mealCalories is already an Int
        }

        return totalCalories
    }



}