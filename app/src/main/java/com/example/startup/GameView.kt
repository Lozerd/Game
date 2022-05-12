package com.example.startup

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.core.GameThread
import com.example.entity.EnemySpaceShip
import com.example.game.R

class GameView(context: Context, attributes: AttributeSet) : SurfaceView(context, attributes),
    SurfaceHolder.Callback {
    private val thread: GameThread
    private var enemySpaceShipsCount: Int = 20
    private var enemySpaceShips: MutableList<EnemySpaceShip> = mutableListOf()
    private val screenHeight = context.resources.displayMetrics.heightPixels

    // BitmapFactory.decodeResource(
    //        context.resources,
    //        R.drawable.shot
    //    )
    init {
        holder.addCallback(this)
        thread = GameThread(holder, this)
    }

    fun update() {
        for (enemySpaceShip in enemySpaceShips) {
//            if (enemySpaceShip.y > screenHeight + enemySpaceShip.h) {
//                enemySpaceShips.remove(enemySpaceShip)
//            } else {
                enemySpaceShip.update()
//            }
        }
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
//        for (index in 0..enemySpaceShips.size){
//           Log.d("debug", "Enemy #${index}: ${enemySpaceShips[index].y}")
//            enemySpaceShips[index].draw(canvas)
//        }
        for (enemySpaceShip in enemySpaceShips) {
            Log.d("debug", "Enemy: ${enemySpaceShip.y}")
            enemySpaceShip.draw(canvas)
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        for (i in 0..enemySpaceShipsCount) {
            enemySpaceShips.add(
                EnemySpaceShip(
                    BitmapFactory.decodeResource(
                        resources,
                        R.drawable.enemy_ship
                    )
                )
            )
        }
        thread.setRunning(true)
        thread.start()

    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        var retry: Boolean = true
        while (retry) {
            try {
                thread.setRunning(false)
                thread.join()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            retry = false
        }
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