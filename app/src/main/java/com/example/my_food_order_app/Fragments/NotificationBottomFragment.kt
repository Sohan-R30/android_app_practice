package com.example.my_food_order_app.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_food_order_app.Adapters.NotificationAdapter
import com.example.my_food_order_app.R
import com.example.my_food_order_app.databinding.FragmentNotificationBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NotificationBottomFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentNotificationBottomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val noticationItemNames = listOf("Your order has been Canceled Successfully", "Order has been taken by the driver", "Congrats Your Order Placed","Your order has been Canceled Successfully", "Order has been taken by the driver", "Congrats Your Order Placed","Your order has been Canceled Successfully", "Order has been taken by the driver", "Congrats Your Order Placed","Your order has been Canceled Successfully", "Order has been taken by the driver", "Congrats Your Order Placed","Your order has been Canceled Successfully", "Order has been taken by the driver", "Congrats Your Order Placed","Your order has been Canceled Successfully", "Order has been taken by the driver", "Congrats Your Order Placed")
        val notifationImages = listOf(R.drawable.sademoji, R.drawable.order_truck, R.drawable.done_icon,R.drawable.sademoji, R.drawable.order_truck, R.drawable.done_icon,R.drawable.sademoji, R.drawable.order_truck, R.drawable.done_icon,R.drawable.sademoji, R.drawable.order_truck, R.drawable.done_icon,R.drawable.sademoji, R.drawable.order_truck, R.drawable.done_icon,R.drawable.sademoji, R.drawable.order_truck, R.drawable.done_icon)

        binding = FragmentNotificationBottomBinding.inflate(layoutInflater, container, false)

        val adapter = NotificationAdapter(ArrayList(noticationItemNames), ArrayList(notifationImages))
        binding.notificationRecycleView.layoutManager = LinearLayoutManager(requireContext())
        binding.notificationRecycleView.adapter = adapter

        return binding.root
    }

    companion object {

    }
}