package com.example.hivekey

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.hivekey.databinding.ActivityProfileBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvBack.setOnClickListener { finish() }

        loadProfile()
    }

    private fun loadProfile() {
        val username = UserManager.getUserField(this, "username")
        val email = UserManager.getUserField(this, "email")
        val role = UserManager.getUserField(this, "role")
        val photoUrl = UserManager.getUserField(this, "photoUrl")
        val createdAt = UserManager.getUserCreatedAt(this)

        binding.tvName.text = username.ifEmpty { "User" }
        binding.tvEmail.text = email
        binding.tvRole.text = role.uppercase().ifEmpty { "USER" }
        binding.tvRoleDetail.text = if (role == "admin") "Administrator" else "Standard User"

        // Format join date
        if (createdAt > 0) {
            val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            binding.tvJoined.text = sdf.format(Date(createdAt))
        }

        // Load profile photo from Cloudinary URL via Glide
        if (photoUrl.isNotEmpty()) {
            Glide.with(this)
                .load(photoUrl)
                .transform(CircleCrop())
                .placeholder(R.drawable.ic_person)
                .into(binding.ivProfilePhoto)
        }
    }
}
