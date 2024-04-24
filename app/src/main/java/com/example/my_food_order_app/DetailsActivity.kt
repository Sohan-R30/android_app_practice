package com.example.my_food_order_app

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.my_food_order_app.Model.CartItemsModel
import com.example.my_food_order_app.databinding.ActivityDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DetailsActivity : AppCompatActivity() {
    private var foodName: String ?= null
    private var foodPrice: String ?= null
    private var foodDescription: String ?= null
    private var foodImage: String ?= null
    private var foodIngredients: String ?= null
    private lateinit var auth: FirebaseAuth


    private lateinit var binding: ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        foodName = intent.getStringExtra("MenuItemName")
        foodPrice = intent.getStringExtra("MenuItemPrice")
        foodDescription = intent.getStringExtra("MenuItemDescription")
        foodImage = intent.getStringExtra("MenuItemImage")
        foodIngredients = intent.getStringExtra("MenuItemIngredients" )

        binding.detialsFoodName.text = foodName
        binding.detailsFoodDescription.text = foodDescription
        binding.ingradientEditTExt.text = foodIngredients
        Glide.with(this).load(Uri.parse(foodImage)).into(binding.detailsFoodImage)


        binding.addToCartOnDetails.setOnClickListener {
            addItemToCart()
        }


        binding.backBtn.setOnClickListener{
            finish()
        }


    }

    private fun addItemToCart() {
        val database = FirebaseDatabase.getInstance().reference
        val userId = auth.currentUser!!.uid

        val cartItem = CartItemsModel(foodName.toString(),foodPrice.toString(),foodDescription.toString(),foodImage.toString(),foodIngredients, 1)

        database.child("user").child(userId).child("cartItems").push().setValue(cartItem).addOnSuccessListener {
            Toast.makeText(this, "Cart Added Successfully",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Cart Added Failed",Toast.LENGTH_SHORT).show()
        }

    }
}