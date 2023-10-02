package com.mbanna.bookshelf.views.fragments.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.mbanna.bookshelf.R
import com.mbanna.bookshelf.databinding.ActivitySplashScreenBinding
import com.mbanna.bookshelf.views.activity.BookShelf
import com.mbanna.bookshelf.views.activity.LoginScreen

class SplashScreen : AppCompatActivity() {
    lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setAnimation()
        splashAnimate()

    }

    private fun setAnimation() {
        val anim = AnimationUtils.loadAnimation(this, R.anim.splash_logo_rotate)
        val anim2 = AnimationUtils.loadAnimation(this, R.anim.splash_logo_inner)

        with(binding) {
            splashInnerLogo.startAnimation(anim2)
            appTitleSplash.startAnimation(anim2)
        }
    }

    private fun splashAnimate(){
        Handler(Looper.getMainLooper()).postDelayed({
            val share = getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
            val isLogin = share.getBoolean("flag", false)
            if (isLogin) {
                val intent = Intent(this, BookShelf::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, LoginScreen::class.java)
                startActivity(intent)
            }
            finish()
        }, 5000)
    }



}