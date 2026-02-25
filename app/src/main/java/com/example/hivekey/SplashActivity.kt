package com.example.hivekey

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.DecelerateInterpolator
import android.view.animation.TranslateAnimation
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.hivekey.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Cloudinary
        CloudinaryHelper.init(this)

        // 1. Fade in main content
        val fadeIn = AlphaAnimation(0f, 1f).apply {
            duration = 1000
            interpolator = DecelerateInterpolator()
            fillAfter = true
        }
        binding.splashContent.startAnimation(fadeIn)

        // 2. Animate decorative dots floating in
        animateDot(binding.tvDot1, 400, 20f)
        animateDot(binding.tvDot2, 600, -15f)
        animateDot(binding.tvDot3, 800, 25f)
        animateDot(binding.tvDot4, 1000, -20f)

        // 3. Pulse the glow ring
        handler.postDelayed({
            val pulse = ObjectAnimator.ofFloat(binding.glowRing, "alpha", 0.2f, 0.6f).apply {
                duration = 800
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.REVERSE
            }
            pulse.start()
        }, 500)

        // 4. Typing animation for tagline
        val tagline = "Secure Access Control"
        handler.postDelayed({
            typeText(binding.tvTagline, tagline, 0)
        }, 800)

        // 5. Animate progress line
        handler.postDelayed({
            val progressLine = binding.progressLine
            val animator = ValueAnimator.ofFloat(0f, 1f).apply {
                duration = 1800
                interpolator = AccelerateDecelerateInterpolator()
                addUpdateListener { anim ->
                    val fraction = anim.animatedValue as Float
                    val params = progressLine.layoutParams
                    val parent = progressLine.parent as View
                    params.width = (parent.width * fraction).toInt()
                    progressLine.layoutParams = params
                }
            }
            animator.start()
        }, 700)

        // 6. Navigate after 3 seconds
        handler.postDelayed({
            val intent = if (UserManager.isLoggedIn(this)) {
                Intent(this, DashboardActivity::class.java)
            } else {
                Intent(this, PanelSelectionActivity::class.java)
            }
            startActivity(intent)
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }, 3000)
    }

    private fun animateDot(dot: View, delay: Long, translateY: Float) {
        handler.postDelayed({
            dot.alpha = 0f
            val fadeIn = AlphaAnimation(0f, 0.6f).apply {
                duration = 600
                fillAfter = true
            }
            dot.startAnimation(fadeIn)

            // Float up and down
            val float = TranslateAnimation(0f, 0f, 0f, translateY).apply {
                duration = 2000
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.REVERSE
                interpolator = AccelerateDecelerateInterpolator()
            }
            handler.postDelayed({ dot.startAnimation(float) }, 600)
        }, delay)
    }

    private fun typeText(tv: TextView, text: String, index: Int) {
        if (index <= text.length) {
            tv.text = text.substring(0, index) + "▌"
            handler.postDelayed({ typeText(tv, text, index + 1) }, 60)
        } else {
            tv.text = text
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}
