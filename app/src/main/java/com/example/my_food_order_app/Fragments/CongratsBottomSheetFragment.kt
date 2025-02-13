package com.example.my_food_order_app.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.my_food_order_app.MainActivity
import com.example.my_food_order_app.R
import com.example.my_food_order_app.databinding.FragmentCongratsBottomSheetBinding
import com.example.my_food_order_app.databinding.FragmentNotificationBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CongratsBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentCongratsBottomSheetBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCongratsBottomSheetBinding.inflate(layoutInflater, container , false)

        binding.goHomeBtn.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    companion object {

    }
}