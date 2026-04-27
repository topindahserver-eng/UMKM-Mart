package com.example.umkmmart.ui.cart

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umkmmart.R
import com.example.umkmmart.databinding.FragmentCartBinding
import java.text.NumberFormat
import java.util.Locale

class CartFragment : Fragment(R.layout.fragment_cart) {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CartViewModel by viewModels()
    private var currentTotalPrice: Long = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCartBinding.bind(view)

        val adapter = CartAdapter(
            onPlusClick = { item ->
                viewModel.updateQuantity(item, item.quantity + 1)
            },
            onMinusClick = { item ->
                viewModel.updateQuantity(item, item.quantity - 1)
            },
            onDeleteClick = { item ->
                viewModel.removeFromCart(item)
            }
        )

        binding.rvCartItems.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }

        viewModel.cartItems.observe(viewLifecycleOwner) { items ->
            adapter.submitList(items)
            updateTotalPrice(items)
            binding.btnCheckout.isEnabled = items.isNotEmpty()
        }

        binding.btnCheckout.setOnClickListener {
            val bundle = Bundle().apply {
                putLong("totalPrice", currentTotalPrice)
            }
            findNavController().navigate(R.id.action_cartFragment_to_paymentFragment, bundle)
        }
    }

    private fun updateTotalPrice(items: List<com.example.umkmmart.data.local.CartItem>) {
        val total = items.sumOf { it.price * it.quantity }
        currentTotalPrice = total.toLong()
        val localeID = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        binding.tvTotalPrice.text = numberFormat.format(total).replace("Rp", "Rp ")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
