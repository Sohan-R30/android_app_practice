package com.example.my_food_order_app.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.AnimationTypes
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.my_food_order_app.Adapters.MenuAdapter
import com.example.my_food_order_app.Model.MenuItemsModel
import com.example.my_food_order_app.R
import com.example.my_food_order_app.databinding.FragmentHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems: MutableList<MenuItemsModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false)

        binding.allMenuView.setOnClickListener{
            val bottomSheetDialog = MenuBottomSheetFragment()
            bottomSheetDialog.show(parentFragmentManager,"test")
        }

        retreivePopularItems()
        return binding.root


    }

    private fun retreivePopularItems() {
        database = FirebaseDatabase.getInstance()
        val foodRef = database.reference.child("menu")

        menuItems = mutableListOf()

        foodRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                menuItems.clear()

                for (foodSpanShot in snapshot.children)
                {
                    val menuItem = foodSpanShot.getValue(MenuItemsModel::class.java)
                    menuItem?.let {
                        menuItems.add(it)
                    }
                }
                randomPopularItems()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun randomPopularItems() {
        val index = menuItems.indices.toList().shuffled()
        val numberOfItem = 6
        val subsetMenuItem = index.take(numberOfItem).map {
            menuItems[it]
        }

        setPopularItemAdapter(subsetMenuItem)
    }

    private fun setPopularItemAdapter(subsetMenuItem: List<MenuItemsModel>) {
        val adapter = MenuAdapter(subsetMenuItem,requireContext())
        binding.PopularRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.PopularRecyclerView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageList = ArrayList<SlideModel>();
        imageList.add( SlideModel(R.drawable.banner1, ScaleTypes.FIT))
        imageList.add( SlideModel(R.drawable.banner1, ScaleTypes.FIT))
        imageList.add( SlideModel(R.drawable.banner1, ScaleTypes.FIT))

        val imageSlider = binding.homeImageSlider
        imageSlider.setImageList(imageList)
        imageSlider.setImageList(imageList,ScaleTypes.FIT)
        imageSlider.setSlideAnimation(AnimationTypes.DEPTH_SLIDE)

        imageSlider.setItemClickListener(object : ItemClickListener {
            override fun onItemSelected(position: Int) {
                val itemPosition = imageList[position]
                val itemMessage = "Selected Image $position"
                Toast.makeText(requireContext(),itemMessage,Toast.LENGTH_SHORT).show()
            }
            override fun doubleClick(position: Int) {

            } })
    }

}