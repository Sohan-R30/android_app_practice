package com.example.my_food_order_app.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.my_food_order_app.databinding.BuyAgainItemBinding

class BuyAgainAdapter (private val buyAgainFoodName: ArrayList<String>, private val buyAgainFoodPrice: ArrayList<String>, private val buyAgainFoodImage: ArrayList<Int>) : RecyclerView.Adapter<BuyAgainAdapter.BuyAgainViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BuyAgainAdapter.BuyAgainViewHolder {
        val binding = BuyAgainItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BuyAgainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BuyAgainAdapter.BuyAgainViewHolder, position: Int) {
        holder.bind(buyAgainFoodName[position],buyAgainFoodPrice[position], buyAgainFoodImage[position])
    }

    override fun getItemCount(): Int {
        return buyAgainFoodName.size
    }

    class BuyAgainViewHolder (private val binding:BuyAgainItemBinding) : RecyclerView.ViewHolder (binding.root)
    {
        fun bind(foodName: String, foodPrice: String, foodImage: Int) {
            binding.buyAFoodName.text = foodName
            binding.buyAFoodPrice.text = foodPrice
            binding.buyAFoodImage.setImageResource(foodImage)
        }

    }
}