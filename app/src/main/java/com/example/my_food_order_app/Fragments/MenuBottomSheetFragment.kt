package com.example.my_food_order_app.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_food_order_app.Adapters.MenuAdapter
import com.example.my_food_order_app.Model.MenuItemsModel
import com.example.my_food_order_app.databinding.FragmentMenuBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MenuBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var database : FirebaseDatabase
    private lateinit var menuItems : MutableList<MenuItemsModel>

    private lateinit var binding: FragmentMenuBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMenuBottomSheetBinding.inflate(inflater, container, false)

        binding.btnBack.setOnClickListener{
            dismiss()
        }

        retrieveMenuItems()

        return binding.root
    }

    private fun retrieveMenuItems() {
        database = FirebaseDatabase.getInstance()
        val foodRef = database.reference.child("menu")

        menuItems = mutableListOf()

        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                menuItems.clear()

                for (foodSpanShot in snapshot.children)
                {
                    val menuItem = foodSpanShot.getValue(MenuItemsModel::class.java)
                    menuItem?.let {
                        menuItems.add(it)
                    }
                }
                setAdapter()
            }


            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    companion object {

    }
    private fun setAdapter() {
        val adapter = MenuAdapter(menuItems,requireContext())
        binding.MenuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.MenuRecyclerView.adapter = adapter
    }
}