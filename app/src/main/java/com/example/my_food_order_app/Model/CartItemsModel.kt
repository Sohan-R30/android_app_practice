package com.example.my_food_order_app.Model

data class CartItemsModel(
    val foodName: String ?= null,
    val foodPrice: String ?= null,
    val foodDescription: String ?= null,
    val foodImage: String ?= null,
    val foodIngredients: String ?= null,
    val foodQuantity: Int ?= null
)
