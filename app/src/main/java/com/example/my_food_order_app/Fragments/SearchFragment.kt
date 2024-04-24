package com.example.my_food_order_app.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_food_order_app.Adapters.MenuAdapter
import com.example.my_food_order_app.R
import com.example.my_food_order_app.databinding.FragmentSearchBinding


class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: MenuAdapter


    private val foodNames = listOf("Momos", "Pizza", "Biriyani", "Pizza", "Biriyani", "Pizza", "Biriyani", "Pizza", "Biriyani", "Pizza", "Biriyani")
    private val price = listOf("$5", "$7", "$10", "$7", "$10", "$7", "$10", "$7", "$10", "$7", "$10")
    private val images = listOf(R.drawable.photo_menu1,R.drawable.photo_menu2,R.drawable.photo_menu4,R.drawable.photo_menu2,R.drawable.photo_menu4,R.drawable.photo_menu2,R.drawable.photo_menu4,R.drawable.photo_menu2,R.drawable.photo_menu4,R.drawable.photo_menu2,R.drawable.photo_menu4)

    private val filterMenuFoodNames = mutableListOf<String>()
    private val filterMenuFoodPrice = mutableListOf<String>()
    private val filterMenuFoodImages = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)

//        adapter = MenuAdapter(filterMenuFoodNames, filterMenuFoodPrice, filterMenuFoodImages, requireContext())

        binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecyclerView.adapter = adapter

        // todo setUpSearchView
        setUpSearchView()

        return binding.root
        // todo show all items
        showAllMenu()

    }

    private fun showAllMenu() {
        filterMenuFoodNames.clear()
        filterMenuFoodPrice.clear()
        filterMenuFoodImages.clear()

        filterMenuFoodNames.add(foodNames.toString())
        filterMenuFoodPrice.add(price.toString())
        filterMenuFoodImages.add(images.toString().toInt())


        adapter.notifyDataSetChanged()
    }

    private fun setUpSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                filterMenuItem(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterMenuItem(newText)
                return true
            }

        })
    }

    private fun filterMenuItem(query: String?) {
        filterMenuFoodNames.clear()
        filterMenuFoodPrice.clear()
        filterMenuFoodImages.clear()

        foodNames.forEachIndexed { index, foodname ->
            if(foodname.contains(query.toString(), ignoreCase = true)){
                filterMenuFoodNames.add(foodname)
                filterMenuFoodPrice.add(price[index])
                filterMenuFoodImages.add(images[index])
            }
        }
        adapter.notifyDataSetChanged()
    }

    companion object {

    }
}