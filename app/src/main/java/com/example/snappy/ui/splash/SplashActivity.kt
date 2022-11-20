package com.example.snappy.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.snappy.R
import com.example.snappy.base.PrivateSharedPrefManager
import com.example.snappy.databinding.ActivityLoginBinding
import com.example.snappy.databinding.ActivitySplashBinding
import com.example.snappy.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    private var privateSharedPrefManager: PrivateSharedPrefManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        privateSharedPrefManager = PrivateSharedPrefManager(this)

        clickListener()
    }

    private fun clickListener() {
        binding.buttonGetStarted.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun showMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}