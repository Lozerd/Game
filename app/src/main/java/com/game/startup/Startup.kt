package com.game.startup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.game.R
import com.game.core.GameView
import java.lang.Exception

class Startup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.startup)
        findViewById<TextView>(R.id.currentLevel).text = getString(R.string.currentLevel, GameView.currentLevel)
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