package com.example.my_food_order_app.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_food_order_app.Adapters.BuyAgainAdapter
import com.example.my_food_order_app.R
import com.example.my_food_order_app.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var adapter: BuyAgainAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentHistoryBinding.inflate(layoutInflater, container, false)
        setUpRecyclerView()
        return binding.root
    }

    private fun setUpRecyclerView(){
        val buyAgainFoodname = arrayListOf("food1", "food2", "food3")
        val buyAgainFoodPrice = arrayListOf("$5", "$10", "$20")
        val buyAgainFoodImage = arrayListOf(R.drawable.photo_menu1, R.drawable.photo_menu2, R.drawable.photo_menu4)

        adapter = BuyAgainAdapter(buyAgainFoodname, buyAgainFoodPrice, buyAgainFoodImage)
        binding.buyAgainRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.buyAgainRecyclerView.adapter = adapter
    }
    companion object {

    }
}