package com.example.startup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.core.GameThread
import com.example.entity.EnemySpaceShip
import com.example.entity.PlayerSpaceShip
import com.example.game.R

class GameView(context: Context, attributes: AttributeSet) : SurfaceView(context, attributes), SurfaceHolder.Callback {
    private val thread: GameThread

    init {
        holder.addCallback(this)
        thread = GameThread(holder, this)
    }

}

/*
var enemySpaceShips: MutableList<EnemySpaceShip> = mutableListOf(
        EnemySpaceShip(context),
        EnemySpaceShip(context),
        EnemySpaceShip(context),
        EnemySpaceShip(context),
        EnemySpaceShip(context)
    )
    var playerSpaceShip = PlayerSpaceShip(context)
    val background: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.background)
    val displayDimensions = context.resources.displayMetrics
    val SCREEN_WIDTH = displayDimensions.widthPixels
    val SCREEN_HEIGHT = displayDimensions.heightPixels
    private var gameStarted = false;


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawRGB(255, 255, 255)
        canvas?.drawBitmap(background, 0.0f, 0.0f, null)


        // First render of enemyShips
        if (!gameStarted) {
            for (enemySpaceShip in enemySpaceShips) {
                canvas?.drawBitmap(
                    enemySpaceShip.getShipBitmap(),
                    enemySpaceShip.x.toFloat(),
                    enemySpaceShip.y.toFloat(),
                    null
                )
            }
            gameStarted = true
        }

        // Check for playerShip life
        if (playerSpaceShip.life == 0) {
            val intent: Intent = Intent(context, GameOver::class.java)
            context.startActivity(intent)
            (context as Activity).finish()
        }

        // Move enemyShips
        for (enemySpaceShip in enemySpaceShips) {
            enemySpaceShip.y += enemySpaceShip.velocity
            if (enemySpaceShip.y > SCREEN_HEIGHT) {
                enemySpaceShips.remove(enemySpaceShip)
            }
        }

        // Render enemyShips
        for (enemySpaceShip in enemySpaceShips) {
            canvas?.drawBitmap(
                enemySpaceShip.getShipBitmap(),
                enemySpaceShip.x.toFloat(),
                enemySpaceShip.y.toFloat(),
                null
            )
        }
    }
 */