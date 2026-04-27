package com.example.umkmmart.ui.payment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.umkmmart.R
import com.example.umkmmart.databinding.FragmentPaymentBinding
import java.text.NumberFormat
import java.util.Locale

class PaymentFragment : Fragment(R.layout.fragment_payment) {

    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPaymentBinding.bind(view)

        val subtotal = arguments?.getLong("totalPrice") ?: 0L
        val serviceFee = 2000L
        val totalPayment = subtotal + serviceFee

        setupUI(subtotal, totalPayment)

        binding.toolbarPayment.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnConfirmPayment.setOnClickListener {
            Toast.makeText(context, "Pembayaran Berhasil! Terima kasih.", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_paymentFragment_to_catalogFragment)
        }
    }

    private fun setupUI(subtotal: Long, total: Long) {
        binding.apply {
            tvTotalItemPrice.text = formatRupiah(subtotal.toDouble())
            tvFinalPrice.text = formatRupiah(total.toDouble())
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
