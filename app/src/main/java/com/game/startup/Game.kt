package com.game.startup

import android.os.Bundle
import android.util.Xml
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.game.R
import com.game.core.GameView

class Game : AppCompatActivity() {
    private var gameView: GameView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameView = GameView(this, Xml.asAttributeSet(resources.getXml(R.xml.game_view)))
        setContentView(gameView)
    }

    override fun onResume() {
        super.onResume()
        setContentView(gameView)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
    }
}