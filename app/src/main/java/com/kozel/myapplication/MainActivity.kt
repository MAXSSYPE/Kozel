package com.kozel.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        timer.start()
    }

    private val timer = object: CountDownTimer(3000, 1000) {
        override fun onTick(millisUntilFinished: Long) {}

        override fun onFinish() {
            val intent = Intent("android.intent.action.menu")
            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        }
    }
}
