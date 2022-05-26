package com.game.startup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.game.R
import com.game.core.GameView
import java.lang.Exception

class GameOver : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_over)
        findViewById<TextView>(R.id.currentLevelGameOver).text = getString(R.string.currentLevelGameOver, GameView.currentLevelInteger)
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