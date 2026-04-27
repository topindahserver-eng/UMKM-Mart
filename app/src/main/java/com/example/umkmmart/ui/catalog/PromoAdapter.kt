package com.example.umkmmart.ui.catalog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.umkmmart.databinding.ItemPromoBinding

data class Promo(val title: String, val subtitle: String, val imageUrl: String)

class PromoAdapter(private val promos: List<Promo>) : RecyclerView.Adapter<PromoAdapter.PromoViewHolder>() {

    inner class PromoViewHolder(private val binding: ItemPromoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(promo: Promo) {
            binding.tvPromoTitle.text = promo.title
            binding.tvPromoSubtitle.text = promo.subtitle
            
            Glide.with(binding.ivPromoBg.context)
                .load(promo.imageUrl)
                .centerCrop()
                .into(binding.ivPromoBg)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromoViewHolder {
        return PromoViewHolder(ItemPromoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PromoViewHolder, position: Int) {
        holder.bind(promos[position])
    }

    override fun getItemCount(): Int = promos.size
}