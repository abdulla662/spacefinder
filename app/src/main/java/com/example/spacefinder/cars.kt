package com.example.spacefinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class cars : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cars)
    }

    fun park(view: View) {
        val a = Intent(this,parkcars::class.java)
        startActivity(a)
    }
    fun move(view: View) {
        val a = Intent(this,movecars::class.java)
        startActivity(a)
    }
}