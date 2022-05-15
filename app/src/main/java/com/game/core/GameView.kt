package com.game.core

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.game.R
import com.game.startup.GameOver
import com.game.entity.*
import com.game.entity.SpaceShip.Companion.getBitmapResource
import com.game.level.GameLevel
import com.game.startup.Startup
import com.game.utils.LinkedSet

class GameView(
    context: Context,
    attributes: AttributeSet
) : SurfaceView(context, attributes), SurfaceHolder.Callback {

    companion object {
        var currentLevel = 1
    }

    private val thread: GameThread
    private val currentLevel: GameLevel = GameLevel(GameView.currentLevel)
    private var playerSpaceShip: PlayerSpaceShip? = null
    private val enemySpaceShipBitmaps: Map<SpaceShipType, Bitmap> = mapOf(
        SpaceShipType.CORVETTE to getBitmapResource(resources, R.drawable.enemy_ship),
        SpaceShipType.PLAYER to getBitmapResource(resources, R.drawable.blank_ship),
        SpaceShipType.INTERDICTOR to getBitmapResource(resources, R.drawable.blank_ship),
        SpaceShipType.VALIANT to getBitmapResource(resources, R.drawable.blank_ship),
        SpaceShipType.DREADNOUGHT to getBitmapResource(resources, R.drawable.blank_ship)
    )

    private var enemySpaceShipsCount = 1
    private var enemySpaceShips = LinkedSet<EnemySpaceShip>()

    private var shots: LinkedSet<Shot> = LinkedSet()
    private val screenHeight: Int by lazy { getDisplayMetrics().heightPixels }
    private val screenWidth: Int by lazy { getDisplayMetrics().widthPixels }


    init {
        holder.addCallback(this)
        thread = GameThread(holder, this)
    }

    private fun getDisplayMetrics(): DisplayMetrics {
        return if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.P) {
            context.resources.displayMetrics
        } else { // Api level < 28
            val metrics = DisplayMetrics()
            @Suppress("Deprecation")
            (context as Activity).windowManager.defaultDisplay.getMetrics(metrics)
            metrics
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
            GameView.currentLevel++
            startNextLevel()
        }
        playerSpaceShip?.draw(canvas)
        enemySpaceShips.forEach { enemySpaceShip -> enemySpaceShip.draw(canvas) }
        shots.forEach { shot -> shot.draw(canvas) }
    }

    private fun startNextLevel() {
        thread.setRunning(false)
        val intent = Intent(context, Startup::class.java)
        context.startActivity(intent)
        (context as Activity).finish()
    }

    private fun stopGame() {
        thread.setRunning(false)
        val intent = Intent(context, GameOver::class.java)
        context.startActivity(intent)
        (context as Activity).finish()
    }

    private fun drawGameObjects() {

        if (enemySpaceShips.isEmpty()) {
            var positionX = 0
            var positionY = 0
            // Draw Corvettes
            for (iterator in 0 until currentLevel.corvetteCount) {
                enemySpaceShips.add(
                    EnemySpaceShip(
                        enemySpaceShipBitmaps[SpaceShipType.CORVETTE]!!,
                        SpaceShipType.CORVETTE,
                        positionX + 50,
                        positionY + 80
                    )
                )
                positionX = if (positionX >= screenWidth) 50 else positionX + 50
                positionY = if (positionX >= screenWidth) positionY + 80 else positionY
            }
            // Draw Interdictors
            for (iterator in 0 until currentLevel.interdictorCount)
                enemySpaceShips.add(
                    EnemySpaceShip(
                        enemySpaceShipBitmaps[SpaceShipType.INTERDICTOR]!!,
                        SpaceShipType.INTERDICTOR,
                    )
                )
            // Draw Valiants
            for (iterator in 0 until currentLevel.valiantCount)
                enemySpaceShips.add(
                    EnemySpaceShip(
                        enemySpaceShipBitmaps[SpaceShipType.INTERDICTOR]!!,
                        SpaceShipType.INTERDICTOR
                    )
                )
            // Draw Dreadnoughts
            for (iterator in 0 until currentLevel.dreadnoghtCount)
                enemySpaceShips.add(
                    EnemySpaceShip(
                        enemySpaceShipBitmaps[SpaceShipType.INTERDICTOR]!!,
                        SpaceShipType.INTERDICTOR
                    )
                )
        }

        if (playerSpaceShip == null) {
            playerSpaceShip = PlayerSpaceShip(
                getBitmapResource(resources, R.drawable.player_spaceship)
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