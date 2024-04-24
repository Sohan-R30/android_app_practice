package com.example.my_food_order_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.my_food_order_app.Fragments.CongratsBottomSheetFragment
import com.example.my_food_order_app.Model.OrderDetails
import com.example.my_food_order_app.databinding.ActivityPayOutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PayOutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPayOutBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var name: String
    private lateinit var address: String
    private lateinit var phone: String
    private lateinit var totalAmount: String

    private lateinit var foodItemName: ArrayList<String>
    private lateinit var foodItemPrice: ArrayList<String>
    private lateinit var foodItemDescription: ArrayList<String>
    private lateinit var foodItemImage: ArrayList<String>
    private lateinit var foodItemNIngredients: ArrayList<String>
    private lateinit var foodItemQuantity: ArrayList<Int>


    private lateinit var database: DatabaseReference
    private lateinit var userId: String





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference()

        binding.backBtnPayout.setOnClickListener{finish()}

        setUserData()

        val intent = intent
        foodItemName = intent.getStringArrayListExtra("foodItemName") as ArrayList<String>
        foodItemPrice = intent.getStringArrayListExtra("foodItemPrice") as ArrayList<String>
        foodItemDescription = intent.getStringArrayListExtra("foodItemDescription") as ArrayList<String>
        foodItemImage = intent.getStringArrayListExtra("foodItemImage") as ArrayList<String>
        foodItemNIngredients = intent.getStringArrayListExtra("foodItemNIngredients") as ArrayList<String>
        foodItemQuantity = intent.getIntegerArrayListExtra("foodItemQuantity") as ArrayList<Int>


        totalAmount = calculateTotalAmount().toString() + "$"

        binding.totalAmountEditText.isEnabled = false
        binding.totalAmountEditText.setText(totalAmount)
        binding.placeMyOrder.setOnClickListener {

            name = binding.nameEditText.text.toString().trim()
            phone = binding.phoneEditText.text.toString().trim()
            address = binding.addressEditText.text.toString().trim()

            if (name.isBlank() || phone.isBlank() || address.isBlank())
            {
                Toast.makeText(this, "FIll All Details", Toast.LENGTH_SHORT).show()
            }
            else
            {
                placeOrder()
            }

        }
    }

    private fun placeOrder() {
        userId = auth.currentUser?.uid?: ""
        val time = System.currentTimeMillis()
        val itemPushKey = database.child("OderDetails").push().key
        val orderDetails = OrderDetails(userId,name,foodItemName,foodItemImage,foodItemPrice,foodItemQuantity,address,totalAmount,phone,false,false,itemPushKey,time)
        val orderRef = database.child("orderDetails").child(itemPushKey!!)
        orderRef.setValue(orderDetails).addOnSuccessListener {
            val bottomSheetDialog = CongratsBottomSheetFragment()
            bottomSheetDialog.show(supportFragmentManager, "Test")
            removeItemFromCart()
            finish()
        }
    }

    private fun removeItemFromCart() {
        val cartItemsRef = database.child("user").child(userId).child("cartItems")
        cartItemsRef.removeValue()
    }

    private fun calculateTotalAmount(): Int {
        var totalAmnt = 0
        for (i in 0 until foodItemPrice.size)
        {
            var price = foodItemPrice[i]
            val lastChar = price.last()
            val priceIntValue = if(lastChar == '$'){
                price.dropLast(1).toInt()
            }
            else{
                price.toInt()
            }

            var quantity = foodItemQuantity[i]
            totalAmnt+= priceIntValue  * quantity
        }
        return totalAmnt
    }

    private fun setUserData() {
        val user = auth.currentUser
        if(user != null)
        {
            userId = user.uid
            val userRef = database.child("user").child(userId)
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists())
                    {
                        val name = snapshot.child("name").getValue(String::class.java)?: ""
                        val address = snapshot.child("address").getValue(String::class.java)?: ""
                        val phone = snapshot.child("phone").getValue(String::class.java)?: ""

                        binding.apply {
                            nameEditText.setText(name)
                            addressEditText.setText(address)
                            phoneEditText.setText(phone)
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
    }
}