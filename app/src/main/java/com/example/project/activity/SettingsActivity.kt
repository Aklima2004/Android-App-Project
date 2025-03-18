package com.example.project.activity

import android.content.Intent
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import com.example.project.profile.ProfileActivity
import com.example.senkazakh.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val generalNotificationSwitch = findViewById<Switch>(R.id.switchGeneralNotification)
        val soundSwitch = findViewById<Switch>(R.id.switchSound)
        val vibrateSwitch = findViewById<Switch>(R.id.switchVibrate)
        val appUpdatesSwitch = findViewById<Switch>(R.id.switchAppUpdates)
        val billReminderSwitch = findViewById<Switch>(R.id.switchBillReminder)
        val promotionSwitch = findViewById<Switch>(R.id.switchPromotion)
        val discountSwitch = findViewById<Switch>(R.id.switchDiscountAvailable)
        val paymentRequestSwitch = findViewById<Switch>(R.id.switchPaymentRequest)


        val switchChangeListener = CompoundButton.OnCheckedChangeListener { switch, isChecked ->
            when (switch.id) {
                R.id.switchGeneralNotification -> {

                }
                R.id.switchSound -> {
                }
                R.id.switchVibrate -> {

                }
                R.id.switchAppUpdates -> {
                }
                R.id.switchBillReminder -> {
                }
                R.id.switchPromotion -> {
                }
                R.id.switchDiscountAvailable -> {
                }
                R.id.switchPaymentRequest -> {
                }
            }
        }


        generalNotificationSwitch.setOnCheckedChangeListener(switchChangeListener)
        soundSwitch.setOnCheckedChangeListener(switchChangeListener)
        vibrateSwitch.setOnCheckedChangeListener(switchChangeListener)
        appUpdatesSwitch.setOnCheckedChangeListener(switchChangeListener)
        billReminderSwitch.setOnCheckedChangeListener(switchChangeListener)
        promotionSwitch.setOnCheckedChangeListener(switchChangeListener)
        discountSwitch.setOnCheckedChangeListener(switchChangeListener)
        paymentRequestSwitch.setOnCheckedChangeListener(switchChangeListener)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.nav_settings

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, TaskListActivity::class.java))
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                R.id.nav_settings -> true
                else -> false
            }
        }

    }
}
