package com.example.startup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.game.R
import java.lang.Exception

class GameOver : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_over)
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