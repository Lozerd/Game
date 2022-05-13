package com.example.startup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.annotation.WorkerThread
import com.example.core.GameThread
import com.example.core.LinkedSet
import com.example.entity.EnemySpaceShip
import com.example.entity.Shot
import com.example.game.R
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class GameView(context: Context, attributes: AttributeSet) : SurfaceView(context, attributes),
    SurfaceHolder.Callback {
    private val thread: GameThread
    private var enemySpaceShipsCount: Int = 1

    //    private var enemySpaceShips: MutableList<EnemySpaceShip> = mutableListOf()
    private var enemySpaceShips: LinkedSet<EnemySpaceShip> = LinkedSet()
    private var Shots: LinkedSet<Shot> = LinkedSet()

    private val screenHeight: Int by lazy { getDisplayMetrics() }


    init {
        holder.addCallback(this)
        thread = GameThread(holder, this)
    }

    private fun getDisplayMetrics(): Int {
        return if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.P) {
            context.resources.displayMetrics.heightPixels
        } else { // Api level < 28
            val metrics = DisplayMetrics()
            @Suppress("Deprecation")
            (context as Activity).windowManager.defaultDisplay.getMetrics(metrics)
            metrics.heightPixels
        }
    }

    //    @OptIn(DelicateCoroutinesApi::class)
    fun update() {
        updateEnemySpaceShips()
        updateShots()
    }

    private fun updateEnemySpaceShips() {
        for (enemySpaceShip: EnemySpaceShip in enemySpaceShips) {
            if (enemySpaceShip.y > screenHeight - enemySpaceShip.h) {
                enemySpaceShips.remove(enemySpaceShip)
            } else {
                enemySpaceShip.update()
                val shot = enemySpaceShip.shoot(context)
                if (shot != null) {
                    Shots.add(shot)
                }
            }
        }
    }

    private fun updateShots() {
        for (shot: Shot in Shots) {
            if (shot.y > screenHeight - shot.h) {
                Shots.remove(shot)
            } else {
                shot.update()
            }
        }
//        Shots.forEach { shot: Shot -> shot.update() }
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        Log.d("debug", "EnemyShips count: ${enemySpaceShips.isEmpty()}")
        if (enemySpaceShips.isEmpty()) {
            thread.setRunning(false)
            val intent = Intent(context, GameOver::class.java)
            context.startActivity(intent)
            (context as Activity).finish()
        }
        enemySpaceShips.forEach { enemySpaceShip -> enemySpaceShip.draw(canvas) }
        Shots.forEach { shot -> shot.draw(canvas) }
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
        var retry = true
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