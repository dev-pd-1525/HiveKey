package com.example.hivekey

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.hivekey.databinding.ActivityDashboardBinding
import java.util.Calendar
import java.util.Locale

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private val LOCATION_PERMISSION_CODE = 100
    private var isAdmin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Determine which panel user selected
        val panelPrefs = getSharedPreferences("hivekey_panel", MODE_PRIVATE)
        val selectedPanel = panelPrefs.getString("selected_panel", "user") ?: "user"
        isAdmin = selectedPanel == "admin"

        setGreeting()
        loadUserProfile()
        setupPanelView()
        setupClickListeners()
        requestLocationPermission()
    }

    private fun setGreeting() {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val greeting = when {
            hour < 12 -> getString(R.string.greeting_morning)
            hour < 17 -> getString(R.string.greeting_afternoon)
            else -> getString(R.string.greeting_evening)
        }
        binding.tvGreeting.text = greeting
    }

    private fun loadUserProfile() {
        val username = UserManager.getUserField(this, "username")
        val email = UserManager.getUserField(this, "email")
        val photoUrl = UserManager.getUserField(this, "photoUrl")

        binding.tvWelcome.text = getString(R.string.welcome_user, username.ifEmpty { "User" })
        binding.tvUserName.text = username.ifEmpty { "User" }
        binding.tvUserEmail.text = email

        // Set role badge based on selected panel
        binding.tvRoleBadge.text = if (isAdmin) "ADMIN" else "USER"

        if (photoUrl.isNotEmpty()) {
            Glide.with(this)
                .load(photoUrl)
                .transform(CircleCrop())
                .placeholder(R.drawable.ic_person)
                .into(binding.ivProfilePic)
        }
    }

    /**
     * Show/hide cards based on which panel was selected.
     * - USER panel: My Passes, My Profile, Developer, Terms
     * - ADMIN panel: ALL cards (Issue Pass, Manage WiFi, Access Logs, Cloud DB + user cards)
     */
    private fun setupPanelView() {
        if (isAdmin) {
            // Show admin-only card rows
            binding.rowAdmin1.visibility = View.VISIBLE
            binding.rowAdmin2.visibility = View.VISIBLE
        } else {
            // Hide admin cards for user panel
            binding.rowAdmin1.visibility = View.GONE
            binding.rowAdmin2.visibility = View.GONE
        }
    }

    private fun setupClickListeners() {
        // === USER CARDS (visible for both panels) ===
        binding.cardMyPasses.setOnClickListener {
            Toast.makeText(this, "My Digital Passes — Coming Soon!", Toast.LENGTH_SHORT).show()
        }

        binding.cardProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        binding.cardDeveloper.setOnClickListener {
            startActivity(Intent(this, DeveloperActivity::class.java))
        }

        binding.cardTerms.setOnClickListener {
            startActivity(Intent(this, TermsActivity::class.java))
        }

        // === ADMIN-ONLY CARDS ===
        binding.cardIssuePass.setOnClickListener {
            Toast.makeText(this, "Issue Digital Pass — Coming Soon!", Toast.LENGTH_SHORT).show()
        }

        binding.cardManageWifi.setOnClickListener {
            Toast.makeText(this, "Manage Wi-Fi — Coming Soon!", Toast.LENGTH_SHORT).show()
        }

        binding.cardAccessLogs.setOnClickListener {
            Toast.makeText(this, "Access Logs — Coming Soon!", Toast.LENGTH_SHORT).show()
        }

        // Admin: Open Cloudinary console in browser
        binding.cardCloudinary.setOnClickListener {
            val url = "https://console.cloudinary.com/"
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }

        // Logout → go back to panel selection
        binding.btnLogout.setOnClickListener {
            UserManager.logout(this)
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, PanelSelectionActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    // ========== LOCATION ==========

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_CODE
            )
        } else {
            getLocation()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_CODE && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            getLocation()
        } else {
            binding.tvLocation.text = "📍 Location permission denied"
        }
    }

    private fun getLocation() {
        try {
            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
            ) return

            val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                ?: locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            if (location != null) {
                resolveLocation(location.latitude, location.longitude)
            } else {
                binding.tvLocation.text = "📍 Fetching location…"
                locationManager.requestSingleUpdate(
                    LocationManager.NETWORK_PROVIDER,
                    { loc -> resolveLocation(loc.latitude, loc.longitude) },
                    null
                )
            }
        } catch (_: Exception) {
            binding.tvLocation.text = "📍 Unable to get location"
        }
    }

    private fun resolveLocation(lat: Double, lon: Double) {
        try {
            val geocoder = Geocoder(this, Locale.getDefault())
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocation(lat, lon, 1)
            if (!addresses.isNullOrEmpty()) {
                val addr = addresses[0]
                val city = addr.locality ?: addr.subAdminArea ?: ""
                val state = addr.adminArea ?: ""
                val text = buildString {
                    append("📍 ")
                    if (city.isNotEmpty()) append(city)
                    if (city.isNotEmpty() && state.isNotEmpty()) append(", ")
                    if (state.isNotEmpty()) append(state)
                }
                runOnUiThread { binding.tvLocation.text = text.ifEmpty { "📍 Location found" } }
            }
        } catch (_: Exception) {
            runOnUiThread { binding.tvLocation.text = "📍 Location found" }
        }
    }
}
