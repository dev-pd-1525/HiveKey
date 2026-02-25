package com.example.hivekey

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hivekey.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty()) {
                showError("Please enter your email")
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                showError("Please enter your password")
                return@setOnClickListener
            }
            if (password.length < 6) {
                showError("Password must be at least 6 characters")
                return@setOnClickListener
            }

            setLoading(true)

            if (UserManager.login(this, email, password)) {
                setLoading(false)
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            } else {
                setLoading(false)
                showError("Invalid email or password")
            }
        }

        binding.tvCreateAccount.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    private fun showError(message: String) {
        binding.tvError.text = message
        binding.tvError.visibility = View.VISIBLE
    }

    private fun setLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        binding.btnLogin.isEnabled = !loading
        binding.tvError.visibility = View.GONE
    }
}