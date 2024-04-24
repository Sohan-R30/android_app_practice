package com.example.my_food_order_app.Adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.my_food_order_app.databinding.CartItemListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CartAdapter (
        private val context: Context,
        private val cartItems: MutableList<String>,
        private val cartItemsPrice: MutableList<String>,
        private val cartImage: MutableList<String> ,
        private val cartDescription: MutableList<String>,
        private val cartIngredients: MutableList<String>,
        private val cartQuantity: MutableList<Int>
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>(){

    private val auth = FirebaseAuth.getInstance()

    init {
        val database = FirebaseDatabase.getInstance()
        val userId = auth.currentUser?.uid?: ""
        val cartItemNumber = cartItems.size

        itemQuantities = IntArray(cartItemNumber){1}
        cartItemRef = database.reference.child("user").child(userId).child("cartItems")
    }

    companion object {
        private var itemQuantities = intArrayOf()
        private lateinit var cartItemRef  : DatabaseReference

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.CartViewHolder {
        val binding = CartItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartAdapter.CartViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return  cartItems.size
    }

    fun getUpdateFoodQuanity(): MutableList<Int> {
        val itemQuanity = mutableListOf<Int>()
        itemQuanity.addAll(cartQuantity)
        return itemQuanity
    }

    inner  class CartViewHolder (private val binding: CartItemListBinding) : RecyclerView.ViewHolder (binding.root){
        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantities[position]
                itemName.text = cartItems[position]
                itemPrice.text = cartItemsPrice[position]

                val uri = Uri.parse(cartImage[position])
                Glide.with(context).load(uri).into(imageCart)

                quantityview.text = quantity.toString()

                minusbtn.setOnClickListener{
                    decreaseQuantity(position)
                }
                plusbtn.setOnClickListener{
                    increaseQuantity(position)
                }
                deletebtn.setOnClickListener{
                    val itemPosition = adapterPosition
                    if(itemPosition != RecyclerView.NO_POSITION)
                    {
                        deleteItem(position)
                    }
                }

            }

        }
        private fun decreaseQuantity(position : Int)
        {
            if(itemQuantities[position] > 1)
            {
                itemQuantities[position] --
                cartQuantity[position] = itemQuantities[position]
                binding.quantityview.text = itemQuantities[position].toString()
            }
        }
        private fun increaseQuantity(position : Int)
        {
            if(itemQuantities[position] < 10)
            {
                itemQuantities[position] ++
                cartQuantity[position] = itemQuantities[position]
                binding.quantityview.text = itemQuantities[position].toString()
            }
        }
        private fun deleteItem(position : Int)
        {
           val poseRetrieve = position
            getUniqueKeyAtPosition(poseRetrieve)
            {
                uniqueKey ->
                if(uniqueKey != null)
                {
                    removeItem(position,uniqueKey)
                }
            }

        }

        private fun removeItem(position: Int, uniqueKey: String) {
            if(uniqueKey != null)
            {
                cartItemRef.child(uniqueKey).removeValue().addOnSuccessListener {
                    cartItems.removeAt(position)
                    cartItemsPrice.removeAt(position)
                    cartImage.removeAt(position)
                    cartQuantity.removeAt(position)
                    cartDescription.removeAt(position)
                    cartIngredients.removeAt(position)

                    Toast.makeText(context, "Successfully Removed item", Toast.LENGTH_SHORT).show()
                    itemQuantities = itemQuantities.filterIndexed{  index, i ->  index != position }.toIntArray()

                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position,cartItems.size)
                }.addOnFailureListener {
                    Toast.makeText(context, "Failed Removed item", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun getUniqueKeyAtPosition(poseRetrieve: Int, onComplete : (String?) -> Unit ) {
            cartItemRef.addListenerForSingleValueEvent(object :ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var uniqueKey :String ?= null
                    
                    snapshot.children.forEachIndexed { index, dataSnapshot ->
                        if(index == poseRetrieve)
                        {
                            uniqueKey = dataSnapshot.key
                            return@forEachIndexed
                        }
                    }
                    onComplete(uniqueKey)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }

    }
}