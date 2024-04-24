package com.example.my_food_order_app.Model

data class UserModel(
    val name : String ?= null,
    val email : String ?= null,
    val password : String ?= null,
    val phone : String ?= null,
    val address : String ?= null
)
