package com.example.my_food_order_app.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.my_food_order_app.DetailsActivity
import com.example.my_food_order_app.databinding.FragmentHomeBinding
import com.example.my_food_order_app.databinding.PopularItemListBinding

class PopularAdapter (private val items:List<String>, private val images:List<Int>,  private val prices:List<String>, private val requreContext: Context) : RecyclerView.Adapter<PopularAdapter.PopularViewHolder>() {

    private val itemClickListner: MenuAdapter.OnClickListener?= null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        return PopularViewHolder(PopularItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        val item = items[position]
        val image = images[position]
        val price = prices[position]
        holder.bind(item,price,image)
    }
    override fun getItemCount(): Int {
        return items.size
    }
    inner class PopularViewHolder (private val binding: PopularItemListBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener{
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION)
                {
                    itemClickListner?.onItemClick(pos)
                }
                //  setOnclickListner to open Details
                val intent = Intent(requreContext, DetailsActivity::class.java)
                intent.putExtra("MenuItemName", items.get(position))
                intent.putExtra("MenuItemImage", images.get(position))
                requreContext.startActivity(intent)
            }
        }

        private  val imagesView = binding.popularFoodImage
        fun bind(item: String, price: String,image: Int) {
            binding.FoodNamePopular.text = item
            binding.pricePopular.text = price
            imagesView.setImageResource(image)
        }

    }
    interface OnClickListener
    {
        fun onItemClick(pos: Int) {

        }
    }
}