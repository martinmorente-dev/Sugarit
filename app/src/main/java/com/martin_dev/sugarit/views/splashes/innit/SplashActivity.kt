package com.martin_dev.sugarit.views.splashes.innit

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.martin_dev.sugarit.R
import com.martin_dev.sugarit.databinding.ActivitySplashBinding
import com.martin_dev.sugarit.views.login.LoginActivity


class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val logoImageView = binding.imageSplash
        val animation = AnimationUtils.loadAnimation(this, R.anim.logo_animation)
        logoImageView.startAnimation(animation)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 4000)
    }
}