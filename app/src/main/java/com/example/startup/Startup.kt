package com.example.startup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.game.R
import java.lang.Exception

class Startup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.startup)
    }

    fun startGame(view: View) {
        try {
            startActivity(Intent(this, Game::class.java))
            finish()
        } catch (e: Exception) {
            Log.d("debug", e.message.toString())
        }

    }
}