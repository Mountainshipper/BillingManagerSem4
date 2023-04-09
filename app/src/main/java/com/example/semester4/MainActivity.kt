package com.example.semester4

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import applicationMain.ui.help.Help


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = Intent(this, Help::class.java)
        startActivity(intent)
    }
}