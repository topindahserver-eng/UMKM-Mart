package com.example.umkmmart.ui.catalog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.umkmmart.R
import com.example.umkmmart.data.local.CartItem
import com.example.umkmmart.databinding.FragmentProductDetailBinding
import com.example.umkmmart.ui.cart.CartViewModel
import com.google.android.material.snackbar.Snackbar
import java.text.NumberFormat
import java.util.Locale

class ProductDetailFragment : Fragment(R.layout.fragment_product_detail) {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!
    private val cartViewModel: CartViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProductDetailBinding.bind(view)

        // Ambil data dari bundle (standard navigation)
        val productId = arguments?.getInt("productId") ?: 0
        val productName = arguments?.getString("productName") ?: ""
        val productPrice = arguments?.getFloat("productPrice")?.toDouble() ?: 0.0
        val productDesc = arguments?.getString("productDesc") ?: ""
        val productImage = arguments?.getString("productImage") ?: ""

        setupUI(productName, productPrice, productDesc, productImage)
        
        binding.toolbarDetail.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // Tombol Keranjang
        binding.btnDetailAddToCart.setOnClickListener {
            addToCart(productId, productName, productPrice, productImage)
        }

        // Tombol Beli Sekarang (Langsung ke Keranjang lalu Checkout)
        binding.btnBuyNow.setOnClickListener {
            addToCart(productId, productName, productPrice, productImage)
            findNavController().navigate(R.id.action_productDetailFragment_to_cartFragment)
        }
    }

    private fun addToCart(id: Int, name: String, price: Double, image: String) {
        val cartItem = CartItem(
            productId = id,
            productName = name,
            price = price,
            quantity = 1,
            imageUrl = image
        )
        cartViewModel.addToCart(cartItem)
        Snackbar.make(binding.root, "$name masuk ke keranjang", Snackbar.LENGTH_LONG)
            .setAction("Lihat") {
                findNavController().navigate(R.id.action_productDetailFragment_to_cartFragment)
            }
            .show()
    }

    private fun setupUI(name: String, price: Double, desc: String, image: String) {
        binding.apply {
            tvDetailName.text = name
            tvDetailPrice.text = formatRupiah(price)
            tvDetailDescription.text = desc
            
            Glide.with(requireContext())
                .load(image)
                .into(ivDetailProduct)
        }
    }

    private fun formatRupiah(number: Double): String {
        val localeID = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        return numberFormat.format(number).replace("Rp", "Rp ")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}