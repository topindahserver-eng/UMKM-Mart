package com.example.umkmmart.ui.payment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.umkmmart.R
import com.example.umkmmart.databinding.FragmentPaymentBinding
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class PaymentFragment : Fragment(R.layout.fragment_payment) {

    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!
    
    private val paymentViewModel: PaymentViewModel by viewModels()

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
            // Menonaktifkan tombol agar tidak diklik berkali-kali
            binding.btnConfirmPayment.isEnabled = false
            
            // Menggunakan lifecycleScope untuk menunggu proses checkout selesai
            lifecycleScope.launch {
                paymentViewModel.checkout()
                
                Toast.makeText(context, "Pembayaran Berhasil! Pesanan diproses.", Toast.LENGTH_LONG).show()
                
                // Berpindah ke Katalog HANYA setelah data tersimpan di riwayat
                findNavController().navigate(R.id.action_paymentFragment_to_catalogFragment)
            }
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
