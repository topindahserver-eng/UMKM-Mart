package com.example.umkmmart.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey
    val productId: Int,
    val productName: String,
    val price: Double,
    val quantity: Int,
    val imageUrl: String
)