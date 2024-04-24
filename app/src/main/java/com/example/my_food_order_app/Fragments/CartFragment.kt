package com.example.my_food_order_app.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_food_order_app.Adapters.CartAdapter
import com.example.my_food_order_app.Adapters.PopularAdapter
import com.example.my_food_order_app.Model.CartItemsModel
import com.example.my_food_order_app.PayOutActivity
import com.example.my_food_order_app.R
import com.example.my_food_order_app.databinding.FragmentCartBinding
import com.example.my_food_order_app.databinding.FragmentHistoryBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var foodName: MutableList<String>
    private lateinit var foodPrice: MutableList<String>
    private lateinit var foodDescription: MutableList<String>
    private lateinit var foodImage: MutableList<String>
    private lateinit var foodIngredient: MutableList<String>
    private lateinit var foodQuantity: MutableList<Int>
    private lateinit var cartAdapter: CartAdapter
    private lateinit var userId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false)


        auth = FirebaseAuth.getInstance()
        retrieveCartItems()


        binding.proceedBtn.setOnClickListener{
            getOrderItemsDetails()
        }
        return binding.root

    }

    private fun getOrderItemsDetails() {
        val orderIdRef :DatabaseReference = database.reference.child("user").child(userId).child("cartItems")

        val foodName = mutableListOf<String>()
        val foodPrice = mutableListOf<String>()
        val foodDescription = mutableListOf<String>()
        val foodImage = mutableListOf<String>()
        val foodIngredient = mutableListOf<String>()
        val foodQuantity = cartAdapter.getUpdateFoodQuanity()

        orderIdRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapShot in snapshot.children){
                    val orderItems = foodSnapShot.getValue(CartItemsModel::class.java)

                    orderItems?.foodName?.let { foodName.add(it) }
                    orderItems?.foodPrice?.let { foodPrice.add(it) }
                    orderItems?.foodDescription?.let { foodDescription.add(it) }
                    orderItems?.foodImage?.let { foodImage.add(it) }
                    orderItems?.foodIngredients?.let { foodIngredient.add(it) }
                    orderItems?.foodQuantity?.let { foodQuantity.add(it) }
                }
                orderNow(foodName, foodPrice, foodDescription, foodImage, foodIngredient, foodQuantity)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Order Failed",Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun orderNow(foodName: MutableList<String>, foodPrice: MutableList<String>, foodDescription: MutableList<String>, foodImage: MutableList<String>, foodIngredient: MutableList<String>, foodQuantity: MutableList<Int>) {
        if(isAdded && context != null)
        {
            val intent = Intent(requireContext(), PayOutActivity::class.java)
            intent.putExtra("foodItemName", foodName as ArrayList<String>)
            intent.putExtra("foodItemPrice", foodPrice as ArrayList<String>)
            intent.putExtra("foodItemDescription", foodDescription as ArrayList<String>)
            intent.putExtra("foodItemImage", foodImage as ArrayList<String>)
            intent.putExtra("foodItemNIngredients", foodIngredient as ArrayList<String>)
            intent.putExtra("foodItemQuantity", foodQuantity as ArrayList<Int>)
            startActivity(intent)
        }
    }

    private fun retrieveCartItems() {
        database = FirebaseDatabase.getInstance()
        userId = auth.currentUser?.uid?: ""

        val foodRef : DatabaseReference = database.reference.child("user").child(userId).child("cartItems")

        foodName = mutableListOf()
        foodPrice = mutableListOf()
        foodDescription = mutableListOf()
        foodImage = mutableListOf()
        foodIngredient = mutableListOf()
        foodQuantity = mutableListOf()

        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(foodSnapShot in snapshot.children)
                {
                    val cartItemsModel = foodSnapShot.getValue(CartItemsModel::class.java)

                    cartItemsModel?.foodName?.let { foodName.add(it) }
                    cartItemsModel?.foodPrice?.let { foodPrice.add(it) }
                    cartItemsModel?.foodDescription?.let { foodDescription.add(it) }
                    cartItemsModel?.foodImage?.let { foodImage.add(it) }
                    cartItemsModel?.foodIngredients?.let { foodIngredient.add(it) }
                    cartItemsModel?.foodQuantity?.let { foodQuantity.add(it) }
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Data Not fetch",Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setAdapter() {
        cartAdapter = CartAdapter(requireContext(),foodName,foodPrice,foodImage,foodDescription,foodIngredient,foodQuantity)
        binding.CartRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL, false)
        binding.CartRecyclerView.adapter = cartAdapter
    }

    companion object {

    }
}