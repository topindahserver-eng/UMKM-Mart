package com.example.umkmmart

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.umkmmart.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Cek apakah dipicu oleh klik notifikasi
        val navigateTo = intent.getStringExtra("navigate_to")
        if (navigateTo == "cart") {
            // Beri sedikit delay agar NavHostFragment siap
            window.decorView.post {
                findNavController(R.id.nav_host_fragment).navigate(R.id.cartFragment)
            }
        }
    }
}