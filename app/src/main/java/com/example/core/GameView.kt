package com.example.core

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.entity.*
import com.example.game.R
import com.example.startup.GameOver

class GameView(
    context: Context,
    attributes: AttributeSet
) : SurfaceView(context, attributes), SurfaceHolder.Callback {
    private val thread: GameThread

    private var playerSpaceShip: PlayerSpaceShip? = null

    private var enemySpaceShipsCount = 1
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
        updatePlayerSpaceShip()
        updateEnemySpaceShips()
        updateShots()
    }

    private fun updatePlayerSpaceShip() {
        playerSpaceShip?.update()
        val shot = playerSpaceShip?.shoot(context)
        if (shot != null) {
            shots.add(shot)
        }
    }

    private fun updateEnemySpaceShips() {
        for (enemySpaceShip: EnemySpaceShip in enemySpaceShips) {
            if (enemySpaceShip.y >= screenHeight - enemySpaceShip.h) {
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
            if (shot.y > screenHeight - shot.h || shot.y == 0) {
                shots.remove(shot)
            } else {
                shot.update()

                if (playerSpaceShip!!.hasCollision(shot)) {
                    playerSpaceShip!!.decrementLife()
                    if (playerSpaceShip!!.isDestroyed()) {
                        Log.d("debug", "Player died")
                        stopGame()
                    }
                    shots.remove(shot)
                }

                for (enemySpaceShip in enemySpaceShips) {
                    if (enemySpaceShip.hasCollision(shot)) {
                        if (enemySpaceShip.isDestroyed()) {
                            Log.d("debug", "EnemySpaceShip died")
                            enemySpaceShips.remove(enemySpaceShip)
                        } else {
                            enemySpaceShip.decrementLife()
                            Log.d("debug", "${enemySpaceShip.getLife()}")
                            shots.remove(shot)
                        }
                    }
                }
            }
        }
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        if (enemySpaceShips.isEmpty()) {
            stopGame()
        }
        playerSpaceShip?.draw(canvas)
        enemySpaceShips.forEach { enemySpaceShip -> enemySpaceShip.draw(canvas) }
        shots.forEach { shot -> shot.draw(canvas) }
    }

    private fun stopGame() {
        thread.setRunning(false)
        val intent = Intent(context, GameOver::class.java)
        context.startActivity(intent)
        (context as Activity).finish()
    }

    private fun drawGameObjects() {
        val enemySpaceShipBitmap = BitmapFactory.decodeResource(
            resources,
            R.drawable.enemy_ship,
            GameSprite.bitmapOptions
        )

        if (enemySpaceShips.isEmpty()) {
            for (i in 0 until enemySpaceShipsCount) {
                enemySpaceShips.add(
                    EnemySpaceShip(enemySpaceShipBitmap)
                )
            }
        }

        if (playerSpaceShip == null) {
            playerSpaceShip = PlayerSpaceShip(
                BitmapFactory.decodeResource(
                    resources,
                    R.drawable.blank_ship,
                    GameSprite.bitmapOptions
                )
            )
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        drawGameObjects()

        thread.setRunning(true)

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

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)

        val touchX = event?.x?.toInt()
        val touchY = event?.y?.toInt()

        when (event?.action) {
            MotionEvent.ACTION_MOVE -> {
                playerSpaceShip?.x = touchX!!
                playerSpaceShip?.y = touchY!!
            }
            MotionEvent.ACTION_UP -> performClick()
        }

        return true
    }
}