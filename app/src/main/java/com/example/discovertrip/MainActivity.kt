package com.example.discovertrip

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.discovertrip.viewmodel.AuthViewModel

class MainActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Criterion II.2: MainActivity uses authViewModel.authenticatedUser.observe to condition UI display
        authViewModel.authenticatedUser.observe(this) { isAuthenticated ->
            if (isAuthenticated) {
                Toast.makeText(this, "Welcome to DiscoverTrip - Session Active", Toast.LENGTH_SHORT).show()
                // TODO: Navigate to Dashboard / Trip List Activity
            } else {
                Toast.makeText(this, "Please Log In", Toast.LENGTH_SHORT).show()
                // TODO: Navigate to Login Activity
            }
        }
    }
}
