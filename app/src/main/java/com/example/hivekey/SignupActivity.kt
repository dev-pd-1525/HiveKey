package com.example.hivekey

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.hivekey.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private var selectedPhotoBitmap: Bitmap? = null

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val photo = result.data?.extras?.get("data") as? Bitmap
            if (photo != null) {
                selectedPhotoBitmap = photo
                binding.ivAvatar.setImageBitmap(photo)
            }
        }
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            if (uri != null) {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                selectedPhotoBitmap = bitmap
                binding.ivAvatar.setImageBitmap(bitmap)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CloudinaryHelper.init(this)

        binding.ivAvatar.setOnClickListener { showImagePickerDialog() }
        binding.btnSelfie.setOnClickListener { launchCamera() }
        binding.tvBack.setOnClickListener { finish() }
        binding.tvSignIn.setOnClickListener { finish() }

        binding.btnSignup.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPass = binding.etConfirmPassword.text.toString().trim()

            if (username.isEmpty()) { showError("Please enter your name"); return@setOnClickListener }
            if (email.isEmpty()) { showError("Please enter your email"); return@setOnClickListener }
            if (!email.contains("@")) { showError("Please enter a valid email"); return@setOnClickListener }
            if (password.isEmpty()) { showError("Please enter a password"); return@setOnClickListener }
            if (password.length < 6) { showError("Password must be at least 6 characters"); return@setOnClickListener }
            if (password != confirmPass) { showError("Passwords do not match"); return@setOnClickListener }

            setLoading(true)

            val panelPrefs = getSharedPreferences("hivekey_panel", MODE_PRIVATE)
            val role = panelPrefs.getString("selected_panel", "user") ?: "user"

            val registered = UserManager.register(this, username, email, password, role)
            if (!registered) {
                setLoading(false)
                showError("An account with this email already exists")
                return@setOnClickListener
            }

            UserManager.login(this, email, password)

            // Upload photo + user log to Cloudinary
            if (selectedPhotoBitmap != null) {
                CloudinaryHelper.uploadImage(
                    this, selectedPhotoBitmap!!,
                    username = username, email = email, role = role,
                    onSuccess = { photoUrl ->
                        UserManager.updateUserField(this, "photoUrl", photoUrl)
                        // Also upload user log
                        CloudinaryHelper.uploadUserLog(this, username, email, role, true) {
                            runOnUiThread { setLoading(false); navigateToDashboard() }
                        }
                    },
                    onError = { _ ->
                        // Photo failed, still upload log
                        CloudinaryHelper.uploadUserLog(this, username, email, role, false) {
                            runOnUiThread { setLoading(false); navigateToDashboard() }
                        }
                    }
                )
            } else {
                // No photo, just upload user log
                CloudinaryHelper.uploadUserLog(this, username, email, role, false) {
                    runOnUiThread { setLoading(false); navigateToDashboard() }
                }
            }
        }
    }

    private fun launchCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intent)
    }

    private fun navigateToDashboard() {
        Toast.makeText(this, "Account created!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, DashboardActivity::class.java))
        finishAffinity()
    }

    private fun showImagePickerDialog() {
        val options = arrayOf("📸 Take Selfie", "🖼️ Choose from Gallery")
        android.app.AlertDialog.Builder(this)
            .setTitle("Profile Photo")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> launchCamera()
                    1 -> {
                        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        galleryLauncher.launch(intent)
                    }
                }
            }
            .show()
    }

    private fun showError(message: String) {
        binding.tvError.text = message
        binding.tvError.visibility = View.VISIBLE
    }

    private fun setLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        binding.btnSignup.isEnabled = !loading
        binding.tvError.visibility = View.GONE
    }
}
