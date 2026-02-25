package com.example.hivekey

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hivekey.databinding.ActivityPanelSelectionBinding

class PanelSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPanelSelectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPanelSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cardAdmin.setOnClickListener {
            // Store selected panel as "admin"
            val prefs = getSharedPreferences("hivekey_panel", MODE_PRIVATE)
            prefs.edit().putString("selected_panel", "admin").apply()

            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.cardUser.setOnClickListener {
            // Store selected panel as "user"
            val prefs = getSharedPreferences("hivekey_panel", MODE_PRIVATE)
            prefs.edit().putString("selected_panel", "user").apply()

            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.tvTerms.setOnClickListener {
            startActivity(Intent(this, TermsActivity::class.java))
        }
    }
}
