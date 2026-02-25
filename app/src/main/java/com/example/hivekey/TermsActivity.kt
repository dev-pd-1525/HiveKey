package com.example.hivekey

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hivekey.databinding.ActivityTermsBinding

class TermsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTermsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvBack.setOnClickListener { finish() }
    }
}
