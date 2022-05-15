package com.game.startup

import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.util.Xml
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.game.core.GameView
import com.game.R
import org.xmlpull.v1.XmlPullParser
import java.lang.Exception

class Game : AppCompatActivity() {
    private var gameView: GameView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameView = GameView(this, Xml.asAttributeSet(resources.getXml(R.xml.game_view)))
        setContentView(gameView)
        setContentView(R.layout.game)
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