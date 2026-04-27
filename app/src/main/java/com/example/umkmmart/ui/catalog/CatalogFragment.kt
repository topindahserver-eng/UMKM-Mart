package com.example.umkmmart.ui.catalog

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.umkmmart.R
import com.example.umkmmart.data.local.CartItem
import com.example.umkmmart.databinding.FragmentCatalogBinding
import com.example.umkmmart.ui.cart.CartViewModel
import com.example.umkmmart.util.NotificationHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class CatalogFragment : Fragment(R.layout.fragment_catalog) {

    private var _binding: FragmentCatalogBinding? = null
    private val binding get() = _binding!!
    
    private val catalogViewModel: CatalogViewModel by viewModels()
    private val cartViewModel: CartViewModel by viewModels()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) { }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCatalogBinding.bind(view)

        checkNotificationPermission()
        setupPromoSlider()
        setupProductList()
        setupNavigation()
        setupSearch()
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun setupPromoSlider() {
        val promoList = listOf(
            Promo(
                "Diskon Produk Lokal!", 
                "Potongan hingga 50% khusus hari ini", 
                "https://images.unsplash.com/photo-1607082348824-0a96f2a4b9da?auto=format&fit=crop&w=800&q=80"
            ),
            Promo(
                "Gratis Ongkir!", 
                "Minimal belanja Rp 50.000 saja", 
                "https://images.unsplash.com/photo-1586880244406-556ebe35f282?auto=format&fit=crop&w=800&q=80"
            ),
            Promo(
                "Cashback Meriah!", 
                "Dapatkan saldo hingga Rp 25.000", 
                "https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?auto=format&fit=crop&w=800&q=80"
            ),
            Promo(
                "Produk Baru UMKM!", 
                "Cek koleksi kerajinan tangan terbaru", 
                "https://images.unsplash.com/photo-1513519245088-0e12902e5a38?auto=format&fit=crop&w=800&q=80"
            )
        )
        val promoAdapter = PromoAdapter(promoList)
        binding.vpPromo.adapter = promoAdapter
    }

    private fun setupProductList() {
        val adapter = ProductAdapter(
            onAddClick = { product ->
                val cartItem = CartItem(
                    productId = product.id,
                    productName = product.name,
                    price = product.price,
                    quantity = 1,
                    imageUrl = product.imageUrl
                )
                cartViewModel.addToCart(cartItem)
                NotificationHelper.showCartNotification(requireContext(), product.name)
                
                Snackbar.make(binding.root, "${product.name} masuk ke keranjang", Snackbar.LENGTH_LONG)
                    .setAction("Lihat") {
                        findNavController().navigate(R.id.action_catalogFragment_to_cartFragment)
                    }
                    .show()
            },
            onItemClick = { product ->
                val bundle = Bundle().apply {
                    putInt("productId", product.id)
                    putString("productName", product.name)
                    putFloat("productPrice", product.price.toFloat())
                    putString("productDesc", product.description)
                    putString("productImage", product.imageUrl)
                }
                findNavController().navigate(R.id.action_catalogFragment_to_productDetailFragment, bundle)
            },
            onEditClick = { product ->
                val bundle = Bundle().apply {
                    putInt("productId", product.id)
                }
                findNavController().navigate(R.id.action_catalogFragment_to_addProductFragment, bundle)
            },
            onDeleteClick = { product ->
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Hapus Produk")
                    .setMessage("Apakah Anda yakin ingin menghapus ${product.name}?")
                    .setNegativeButton("Batal", null)
                    .setPositiveButton("Hapus") { _, _ ->
                        catalogViewModel.deleteProduct(product)
                        Toast.makeText(requireContext(), "${product.name} dihapus", Toast.LENGTH_SHORT).show()
                    }
                    .show()
            }
        )

        binding.rvProducts.apply {
            layoutManager = GridLayoutManager(context, 2)
            this.adapter = adapter
        }

        catalogViewModel.products.observe(viewLifecycleOwner) { products ->
            adapter.submitList(products)
        }
    }

    private fun setupSearch() {
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                catalogViewModel.setSearchQuery(newText ?: "")
                return true
            }
        })
    }

    private fun setupNavigation() {
        binding.btnCart.setOnClickListener {
            findNavController().navigate(R.id.action_catalogFragment_to_cartFragment)
        }

        binding.btnProfile.setOnClickListener {
            findNavController().navigate(R.id.action_catalogFragment_to_profileFragment)
        }

        binding.btnAddProduct.setOnClickListener {
            findNavController().navigate(R.id.action_catalogFragment_to_addProductFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}