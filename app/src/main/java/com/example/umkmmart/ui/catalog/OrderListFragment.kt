package com.example.umkmmart.ui.catalog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umkmmart.R
import com.example.umkmmart.data.local.AppDatabase
import com.example.umkmmart.databinding.FragmentOrderListBinding

class OrderListFragment : Fragment(R.layout.fragment_order_list) {

    private var _binding: FragmentOrderListBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentOrderListBinding.bind(view)

        // Menggunakan OrderAdapter yang baru dibuat
        val adapter = OrderAdapter()

        binding.rvOrders.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }

        val db = AppDatabase.getDatabase(requireContext())
        // Mengamati data dari tabel order_items
        db.orderDao().getAllOrders().asLiveData().observe(viewLifecycleOwner) { orders ->
            if (orders.isEmpty()) {
                binding.tvEmptyOrders.visibility = View.VISIBLE
            } else {
                binding.tvEmptyOrders.visibility = View.GONE
                adapter.submitList(orders)
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