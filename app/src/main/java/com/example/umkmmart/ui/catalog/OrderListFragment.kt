package com.example.umkmmart.ui.catalog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umkmmart.R
import com.example.umkmmart.data.local.AppDatabase
import com.example.umkmmart.data.local.CartItem
import com.example.umkmmart.databinding.FragmentOrderListBinding
import com.example.umkmmart.ui.cart.CartAdapter

class OrderListFragment : Fragment(R.layout.fragment_order_list) {

    private var _binding: FragmentOrderListBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentOrderListBinding.bind(view)

        // Kita gunakan CartAdapter yang sudah ada untuk menampilkan daftar pesanan (Read-only)
        val adapter = CartAdapter(
            onPlusClick = {}, // Disable aksi di riwayat
            onMinusClick = {},
            onDeleteClick = {}
        )

        binding.rvOrders.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }

        val db = AppDatabase.getDatabase(requireContext())
        db.orderDao().getAllOrders().asLiveData().observe(viewLifecycleOwner) { orders ->
            if (orders.isEmpty()) {
                binding.tvEmptyOrders.visibility = View.VISIBLE
            } else {
                binding.tvEmptyOrders.visibility = View.GONE
                // Konversi OrderItem ke CartItem untuk adapter
                val displayItems = orders.map { 
                    CartItem(it.productId, it.productName, it.price, it.quantity, it.imageUrl) 
                }
                adapter.submitList(displayItems)
            }
        }

        binding.toolbarOrders.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}