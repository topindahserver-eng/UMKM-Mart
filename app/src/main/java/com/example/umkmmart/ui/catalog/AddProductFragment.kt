package com.example.umkmmart.ui.catalog

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.umkmmart.R
import com.example.umkmmart.data.local.AppDatabase
import com.example.umkmmart.data.local.Product
import com.example.umkmmart.databinding.FragmentAddProductBinding
import kotlinx.coroutines.launch

class AddProductFragment : Fragment(R.layout.fragment_add_product) {

    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!
    private var selectedImageUri: Uri? = null
    private var productId: Int? = null
    private var existingProduct: Product? = null

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            selectedImageUri = uri
            binding.ivProductPreview.setImageURI(uri)
            binding.llPlaceholder.visibility = View.GONE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddProductBinding.bind(view)

        productId = arguments?.getInt("productId")
        if (productId != null && productId != 0) {
            loadProductData(productId!!)
        }

        binding.toolbarAddProduct.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.cvSelectImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.btnUpload.setOnClickListener {
            saveProductToLocal()
        }
    }

    private fun loadProductData(id: Int) {
        lifecycleScope.launch {
            try {
                val db = AppDatabase.getDatabase(requireContext())
                existingProduct = db.productDao().getProductById(id)
                
                existingProduct?.let { product ->
                    binding.apply {
                        toolbarAddProduct.title = "Edit Produk"
                        etProductName.setText(product.name)
                        etProductPrice.setText(product.price.toLong().toString())
                        etProductDesc.setText(product.description)
                        btnUpload.text = "Update Produk"
                        
                        if (product.imageUrl.isNotEmpty()) {
                            Glide.with(requireContext())
                                .load(product.imageUrl)
                                .into(ivProductPreview)
                            llPlaceholder.visibility = View.GONE
                        }
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Gagal memuat data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveProductToLocal() {
        val name = binding.etProductName.text.toString().trim()
        val priceStr = binding.etProductPrice.text.toString().trim()
        val desc = binding.etProductDesc.text.toString().trim()

        // Validasi: Jika tambah baru (existingProduct == null), image wajib diisi.
        // Jika edit, image tidak wajib (akan pakai yang lama jika tidak diubah).
        if (name.isEmpty() || priceStr.isEmpty() || desc.isEmpty()) {
            Toast.makeText(context, "Nama, harga, dan deskripsi harus diisi!", Toast.LENGTH_SHORT).show()
            return
        }
        
        if (existingProduct == null && selectedImageUri == null) {
            Toast.makeText(context, "Pilih foto produk terlebih dahulu!", Toast.LENGTH_SHORT).show()
            return
        }

        val price = priceStr.toDoubleOrNull() ?: 0.0
        
        val productToSave = if (existingProduct != null) {
            existingProduct!!.copy(
                name = name,
                description = desc,
                price = price,
                imageUrl = selectedImageUri?.toString() ?: existingProduct!!.imageUrl
            )
        } else {
            Product(
                id = System.currentTimeMillis().toInt(),
                name = name,
                description = desc,
                price = price,
                imageUrl = selectedImageUri.toString()
            )
        }

        binding.btnUpload.isEnabled = false
        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val db = AppDatabase.getDatabase(requireContext())
                if (existingProduct != null) {
                    db.productDao().updateProduct(productToSave)
                    Toast.makeText(context, "Produk berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                } else {
                    db.productDao().insertProduct(productToSave)
                    Toast.makeText(context, "Produk berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                }
                findNavController().navigateUp()
            } catch (e: Exception) {
                Toast.makeText(context, "Gagal menyimpan: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                binding.btnUpload.isEnabled = true
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
