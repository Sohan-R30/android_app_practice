package com.example.my_food_order_app.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.my_food_order_app.databinding.FragmentNotificationBottomBinding
import com.example.my_food_order_app.databinding.NotificationItemListBinding

class NotificationAdapter (private  var notificationItemsName: ArrayList<String>, private  var notificationImage: ArrayList<Int>) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationAdapter.NotificationViewHolder {
        val binding = NotificationItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationAdapter.NotificationViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return notificationItemsName.size
    }
    inner class NotificationViewHolder(private val binding: NotificationItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                NotificationTextView.text = notificationItemsName[position]
                NotificationImageView.setImageResource(notificationImage[position])
            }
        }
    }
}