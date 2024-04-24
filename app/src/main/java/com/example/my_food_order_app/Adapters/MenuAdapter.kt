package com.example.my_food_order_app.Adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.my_food_order_app.DetailsActivity
import com.example.my_food_order_app.Model.MenuItemsModel
import com.example.my_food_order_app.databinding.MenuItemListBinding

class MenuAdapter(private val menuItems: List<MenuItemsModel>, private val requreContext: Context) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    private val itemClickListner: OnClickListener ?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuAdapter.MenuViewHolder {
        val binding = MenuItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuAdapter.MenuViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return menuItems.size
    }

    inner class MenuViewHolder (private val binding: MenuItemListBinding) : RecyclerView.ViewHolder (binding.root) {
        init {
            binding.root.setOnClickListener{
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION)
                {
                   openDetailsActivity(pos)
                }

            }
        }

        private fun openDetailsActivity(pos: Int) {
            val menuItem = menuItems[pos]
            val intent = Intent(requreContext, DetailsActivity::class.java).apply {
                putExtra("MenuItemName", menuItem.foodName)
                putExtra("MenuItemPrice", menuItem.foodPrice)
                putExtra("MenuItemDescription", menuItem.foodDescription)
                putExtra("MenuItemImage", menuItem.foodImage)
                putExtra("MenuItemIngredients", menuItem.foodIngredients)
            }
            requreContext.startActivity(intent)
        }

        fun bind(position: Int) {
            val menuItem = menuItems[position]
            binding.apply {

                FoodNameMenu.text = menuItem.foodName
                priceMenu.text = menuItem.foodPrice

                val uri = Uri.parse(menuItem.foodImage)
                Glide.with(requreContext).load(uri).into(menuItemImage)

            }
        }

    }
    interface OnClickListener
    {
        fun onItemClick(pos: Int) {

        }
    }
}



