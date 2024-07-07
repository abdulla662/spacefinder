package com.example.spacefinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class enjoy : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enjoy)
    }

    fun home(view: View) {
        val a = Intent(this,cars::class.java)
        startActivity(a)
    }
}