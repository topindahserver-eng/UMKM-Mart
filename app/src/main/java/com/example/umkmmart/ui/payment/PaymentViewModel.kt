package com.example.umkmmart.ui.payment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.umkmmart.data.local.AppDatabase
import com.example.umkmmart.data.local.OrderItem
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

class PaymentViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val cartDao = db.cartDao()
    private val orderDao = db.orderDao()

    // Diubah menjadi suspend function agar bisa ditunggu (sequential)
    suspend fun checkout() = withContext(Dispatchers.IO) {
        val cartItems = cartDao.getAllCartItems().first()
        
        if (cartItems.isNotEmpty()) {
            val orderItems = cartItems.map { cartItem ->
                OrderItem(
                    productId = cartItem.productId,
                    productName = cartItem.productName,
                    price = cartItem.price,
                    quantity = cartItem.quantity,
                    imageUrl = cartItem.imageUrl
                )
            }
            
            // Simpan ke riwayat
            orderDao.insertOrders(orderItems)
            
            // Kosongkan keranjang
            cartDao.clearCart()
        }
    }
}