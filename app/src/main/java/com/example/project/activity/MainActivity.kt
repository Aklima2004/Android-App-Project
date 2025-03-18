package com.example.project.activity

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.activity.ComponentActivity
import com.example.project.profile.LoginActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layoutId = resources.getIdentifier("activity_main", "layout", packageName)
        setContentView(layoutId)

        val imageViewId = resources.getIdentifier("logoImageView", "id", packageName)
        val logoImageView: ImageView = findViewById(imageViewId)

        val animId = resources.getIdentifier("cool_logo_animation", "anim", packageName)
        val animation = AnimationUtils.loadAnimation(this, animId)

        animation.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
            override fun onAnimationStart(animation: android.view.animation.Animation?) {}

            override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun onAnimationRepeat(animation: android.view.animation.Animation?) {}
        })

        logoImageView.startAnimation(animation)
    }
}
