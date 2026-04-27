package com.example.umkmmart.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items")
    fun getAllCartItems(): Flow<List<CartItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToCart(item: CartItem)

    @Update
    suspend fun updateQuantity(item: CartItem)

    @Delete
    suspend fun removeFromCart(item: CartItem)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}