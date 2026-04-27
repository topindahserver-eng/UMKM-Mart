package com.example.umkmmart.ui.catalog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.umkmmart.R
import com.example.umkmmart.data.local.Product
import com.example.umkmmart.databinding.ItemProductBinding
import java.text.NumberFormat
import java.util.Locale

class ProductAdapter(
    private val onAddClick: (Product) -> Unit,
    private val onItemClick: (Product) -> Unit,
    private val onEditClick: (Product) -> Unit,
    private val onDeleteClick: (Product) -> Unit
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.apply {
                tvProductName.text = product.name
                tvProductPrice.text = formatRupiah(product.price)
                
                Glide.with(ivProduct.context)
                    .load(product.imageUrl)
                    .placeholder(android.R.color.darker_gray)
                    .into(ivProduct)

                btnAddToCart.setOnClickListener {
                    onAddClick(product)
                }

                btnMore.setOnClickListener { view ->
                    val popup = PopupMenu(view.context, view)
                    popup.menuInflater.inflate(R.menu.menu_product, popup.menu)
                    popup.setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.action_edit -> {
                                onEditClick(product)
                                true
                            }
                            R.id.action_delete -> {
                                onDeleteClick(product)
                                true
                            }
                            else -> false
                        }
                    }
                    popup.show()
                }

                root.setOnClickListener {
                    onItemClick(product)
                }
            }
        }

        private fun formatRupiah(number: Double): String {
            val localeID = Locale("in", "ID")
            val numberFormat = NumberFormat.getCurrencyInstance(localeID)
            return numberFormat.format(number).replace("Rp", "Rp ")
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
}
