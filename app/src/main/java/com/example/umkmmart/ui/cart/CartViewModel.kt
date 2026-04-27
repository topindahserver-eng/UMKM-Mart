package com.example.umkmmart.ui.cart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.umkmmart.data.local.AppDatabase
import com.example.umkmmart.data.local.CartItem
import kotlinx.coroutines.launch

class CartViewModel(application: Application) : AndroidViewModel(application) {
    private val cartDao = AppDatabase.getDatabase(application).cartDao()
    
    val cartItems: LiveData<List<CartItem>> = cartDao.getAllCartItems().asLiveData()

    fun addToCart(item: CartItem) {
        viewModelScope.launch {
            cartDao.addToCart(item)
        }
    }

    fun updateQuantity(item: CartItem, newQuantity: Int) {
        viewModelScope.launch {
            if (newQuantity <= 0) {
                cartDao.removeFromCart(item)
            } else {
                cartDao.updateQuantity(item.copy(quantity = newQuantity))
            }
        }
    }

    fun removeFromCart(item: CartItem) {
        viewModelScope.launch {
            cartDao.removeFromCart(item)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            cartDao.clearCart()
        }
    }
}