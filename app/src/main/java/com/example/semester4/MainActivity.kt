package com.example.semester4

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import applicationMain.MainStart
import applicationMain.StartApplication
import login.SignUp


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = Intent(this, StartApplication::class.java)
        startActivity(intent)
    }
}