package com.example.my_food_order_app.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.my_food_order_app.Model.UserModel
import com.example.my_food_order_app.R
import com.example.my_food_order_app.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference


    private lateinit var name: String
    private lateinit var address: String
    private lateinit var phone: String
    private lateinit var email: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference()

        setUserdata()

        binding.saveInfoBtn.setOnClickListener {
            val name = binding.profileName.text.toString()
            val address = binding.profileAddress.text.toString()
            val email = binding.profileEmail.text.toString()
            val phone = binding.profilePhone.text.toString()

            updateUserDate(name,address,email,phone)
        }
        return binding.root


    }

    private fun updateUserDate(name: String, address: String, email: String, phone: String) {
        val userId = auth.currentUser?.uid
        if (userId != null)
        {
            val userRef = database.child("user").child(userId)
            val userData = hashMapOf(
                "name" to name,
                "address" to address,
                "email" to email,
                "phone" to phone
            )
            userRef.setValue(userData).addOnSuccessListener {
                Toast.makeText(requireContext(),"Save Info Successfully", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(requireContext(),"Save Info Failed", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun setUserdata() {
        val user = auth.currentUser
        if(user != null)
        {
            val userId = user.uid
            val userRef = database.child("user").child(userId)
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists())
                    {
                        val userProfile = snapshot.getValue(UserModel::class.java)
                        if(userProfile != null)
                        {
                            binding.profileName.setText(userProfile.name)
                            binding.profileAddress.setText(userProfile.address)
                            binding.profilePhone.setText(userProfile.phone)
                            binding.profileEmail.setText(userProfile.email)
                        }

                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
    }

    companion object {
    }
}