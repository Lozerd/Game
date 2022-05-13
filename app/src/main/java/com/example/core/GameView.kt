package com.example.core

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
import com.example.entity.EnemySpaceShip
import com.example.entity.Shot
import com.example.game.R
import com.example.startup.GameOver

class GameView(context: Context, attributes: AttributeSet) : SurfaceView(context, attributes),
    SurfaceHolder.Callback {
    private val thread: GameThread
    private var enemySpaceShipsCount = 20

    private var enemySpaceShips = LinkedSet<EnemySpaceShip>()
    private var shots: LinkedSet<Shot> = LinkedSet()

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
                    shots.add(shot)
                }
            }
        }
    }

    private fun updateShots() {
        for (shot: Shot in shots) {
            if (shot.y > screenHeight - shot.h) {
                shots.remove(shot)
            } else {
                shot.update()
            }
        }
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
        shots.forEach { shot -> shot.draw(canvas) }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        if (enemySpaceShips.isEmpty()) {
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
        }
        thread.setRunning(true)
        Log.d("debug", "${thread.state}")
        if (thread.state == Thread.State.NEW || thread.state == Thread.State.TERMINATED) {
            thread.start()
        }
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