package com.example.umkmmart.ui.catalog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.umkmmart.data.local.OrderItem
import com.example.umkmmart.databinding.ItemCartBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class OrderAdapter : ListAdapter<OrderItem, OrderAdapter.OrderViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class OrderViewHolder(private val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OrderItem) {
            binding.apply {
                tvCartProductName.text = item.productName
                tvCartProductPrice.text = formatRupiah(item.price * item.quantity)
                tvQuantity.text = "${item.quantity}x"
                
                // Sembunyikan tombol edit karena ini riwayat
                btnPlus.visibility = android.view.View.GONE
                btnMinus.visibility = android.view.View.GONE
                btnDelete.visibility = android.view.View.GONE

                Glide.with(ivCartProduct.context)
                    .load(item.imageUrl)
                    .into(ivCartProduct)
            }
        }

        private fun formatRupiah(number: Double): String {
            val localeID = Locale("in", "ID")
            return NumberFormat.getCurrencyInstance(localeID).format(number).replace("Rp", "Rp ")
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<OrderItem>() {
        override fun areItemsTheSame(oldItem: OrderItem, newItem: OrderItem) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: OrderItem, newItem: OrderItem) = oldItem == newItem
    }
}