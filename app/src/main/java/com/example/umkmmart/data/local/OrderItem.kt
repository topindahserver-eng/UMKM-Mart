package com.example.umkmmart.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_items")
data class OrderItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val productId: Int,
    val productName: String,
    val price: Double,
    val quantity: Int,
    val imageUrl: String,
    val category: String = "Lokal",
    val orderDate: Long = System.currentTimeMillis()
)