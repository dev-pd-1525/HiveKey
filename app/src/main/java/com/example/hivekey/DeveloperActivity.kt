package com.example.hivekey

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hivekey.databinding.ActivityDeveloperBinding

class DeveloperActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeveloperBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeveloperBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvBack.setOnClickListener { finish() }
    }
}
