package com.example.my_food_order_app.Model

import android.os.Parcel
import android.os.Parcelable
import java.util.ArrayList

class OrderDetails (): Parcelable {
    var userId: String ?= null
    var userName: String ?= null
    var foodNames: MutableList<String> ?= null
    var foodImages: MutableList<String> ?= null
    var foodPrices: MutableList<String> ?= null
    var foodQuantities: MutableList<String> ?= null
    var address: String ?= null
    var totalPrice: String ?= null
    var phoneNUmber: String ?= null
    var orderAccepted: Boolean = false
    var paymentReceived: Boolean = false
    var itemPushKey: String ?= null
    var currentTime: Long = 0

    constructor(parcel: Parcel) : this() {
        userId = parcel.readString()
        userName = parcel.readString()
        address = parcel.readString()
        totalPrice = parcel.readString()
        phoneNUmber = parcel.readString()
        orderAccepted = parcel.readByte() != 0.toByte()
        paymentReceived = parcel.readByte() != 0.toByte()
        itemPushKey = parcel.readString()
        currentTime = parcel.readLong()
    }

    constructor(
        userId: String,
        name: String,
        foodItemName: ArrayList<String>,
        foodItemImage: ArrayList<String>,
        foodItemPrice: ArrayList<String>,
        foodItemQuantity: ArrayList<Int>,
        address: String,
        totalAmount: String,
        phone: String,
        b: Boolean,
        b1: Boolean,
        itemPushKey: String?,
        time: Long
    ) : this()

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(userName)
        parcel.writeString(address)
        parcel.writeString(totalPrice)
        parcel.writeString(phoneNUmber)
        parcel.writeByte(if (orderAccepted) 1 else 0)
        parcel.writeByte(if (paymentReceived) 1 else 0)
        parcel.writeString(itemPushKey)
        parcel.writeLong(currentTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderDetails> {
        override fun createFromParcel(parcel: Parcel): OrderDetails {
            return OrderDetails(parcel)
        }

        override fun newArray(size: Int): Array<OrderDetails?> {
            return arrayOfNulls(size)
        }
    }


}