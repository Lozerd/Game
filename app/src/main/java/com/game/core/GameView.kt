package com.game.core

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.game.R
import com.game.entity.*
import com.game.entity.SpaceShip.Companion.getBitmapResource
import com.game.level.GameLevel
import com.game.level.GameMap
import com.game.startup.GameOver
import com.game.startup.Startup
import com.game.utils.LinkedSet
import kotlinx.coroutines.*

class GameView(
    context: Context,
    attributes: AttributeSet
) : SurfaceView(context, attributes), SurfaceHolder.Callback {

    companion object {
        var currentLevelInteger = 1
    }

    private val gameThread: GameThread
    private val updateThread: UpdateThread
    private val updateCoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val currentLevel: GameLevel = GameLevel(currentLevelInteger)
    private var playerSpaceShip: PlayerSpaceShip? = null
    private val enemySpaceShipBitmaps: Map<SpaceShipType, Bitmap> = mapOf(
        SpaceShipType.CORVETTE to getBitmapResource(resources, R.drawable.corvette),
        SpaceShipType.PLAYER to getBitmapResource(resources, R.drawable.player_spaceship),
        SpaceShipType.INTERDICTOR to getBitmapResource(resources, R.drawable.interdictor),
        SpaceShipType.VALIANT to getBitmapResource(resources, R.drawable.valiant),
        SpaceShipType.DREADNOUGHT to getBitmapResource(resources, R.drawable.dreadnought_big)
    )

    private var enemySpaceShips = LinkedSet<EnemySpaceShip>()

    private var shots: LinkedSet<Shot> = LinkedSet()
    private val screenHeight: Int by lazy { getDisplayMetrics().heightPixels }
    private val screenWidth: Int by lazy { getDisplayMetrics().widthPixels }


    init {
        holder.addCallback(this)
        gameThread = GameThread(holder, this)
        updateThread = UpdateThread(this)
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
        /* Method updates playerSpaceShip and its shooting */
        playerSpaceShip?.update()
        val shot = playerSpaceShip?.shoot(context)
        if (shot != null) {
            shots.add(shot)
        }
    }

    private fun updateEnemySpaceShips() {
        /* Method updates enemySpaceShip and its shooting */
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
        /* Method updates all shots and checks collisions */
        for (shot: Shot in shots) {
            if (shot.y > screenHeight - shot.h || shot.y == 0) {
                shots.remove(shot)
            } else {
                shot.update()

                updateCoroutineScope.launch {
                    if (!enemySpaceShips.none { playerSpaceShip?.hasCollision(it) == true }){
                        playerSpaceShip!!.decrementLife()
                        if (playerSpaceShip!!.isDestroyed()) {
                            stopGame()
                        }
                    }
                }

                updateCoroutineScope.launch {
                    // Checking player collision
                    if (playerSpaceShip!!.hasCollision(shot)) {
                        playerSpaceShip!!.decrementLife()
                        if (playerSpaceShip!!.isDestroyed()) {
                            stopGame()
                        }
                        shots.remove(shot)
                    }
                }

                updateCoroutineScope.launch {
                    // Checking enemySpaceShip collisions
                    for (enemySpaceShip in enemySpaceShips) {
                        if (enemySpaceShip.hasCollision(shot)) {
                            enemySpaceShip.decrementLife()
                            if (enemySpaceShip.isDestroyed()) {
                                enemySpaceShips.remove(enemySpaceShip)
                            }
                            shots.remove(shot)
                        }
                    }
                }
            }
        }
    }

    override fun draw(canvas: Canvas) {
        /* Method that draw every frame on canvas */
        super.draw(canvas)

        if (enemySpaceShips.isEmpty()) {
            currentLevelInteger++
            enemySpaceShips = LinkedSet<EnemySpaceShip>()
            shots = LinkedSet<Shot>()
            startNextLevel()
        }
        playerSpaceShip?.draw(canvas)
        enemySpaceShips.forEach { enemySpaceShip -> enemySpaceShip.draw(canvas) }
        shots.forEach { shot -> shot.draw(canvas) }
    }

    private fun startNextLevel() {
        /* Method redirects to next level through Intent to Startup class */
        gameThread.setRunning(false)
        updateThread.setRunning(false)
        context.startActivity(Intent(context, Startup::class.java))
        (context as Activity).finish()
    }

    private fun stopGame() {
        /* Method redirects to Game Over screen */
        gameThread.setRunning(false)
        updateThread.setRunning(false)
        context.startActivity(Intent(context, GameOver::class.java))
        (context as Activity).finish()
    }

    private fun addGameObjects() {
//        val gameMap = GameMap(context, screenWidth)
//        gameMap.load(enemySpaceShips, currentLevel)
        /* Add game objects to their containers  */
        if (enemySpaceShips.isEmpty()) {
            var positionX = 100
            var positionY = 0
            // Draw Corvettes
            for (iterator in 0 until currentLevel.corvetteCount) {
                enemySpaceShips.add(
                    EnemySpaceShip(
                        enemySpaceShipBitmaps[SpaceShipType.CORVETTE]!!,
                        SpaceShipType.CORVETTE,
                        positionX,
                        positionY
                    )
                )
                positionY = if (positionX + 100 >= screenWidth) positionY + 100 else positionY
                positionX = if (positionX + 100 >= screenWidth) 100 else positionX + 100
            }
            // Draw Interdictors
            for (iterator in 0 until currentLevel.interdictorCount) {
                enemySpaceShips.add(
                    EnemySpaceShip(
                        enemySpaceShipBitmaps[SpaceShipType.INTERDICTOR]!!,
                        SpaceShipType.INTERDICTOR,
                        positionX,
                        positionY
                    )
                )
                positionY = if (positionX + 150 >= screenWidth) positionY + 100 else positionY
                positionX = if (positionX + 150 >= screenWidth) 150 else positionX + 150
            }
            // Draw Valiants
            for (iterator in 0 until currentLevel.valiantCount) {
                enemySpaceShips.add(
                    EnemySpaceShip(
                        enemySpaceShipBitmaps[SpaceShipType.VALIANT]!!,
                        SpaceShipType.VALIANT,
                        positionX,
                        positionY,
                    )
                )
                positionY = if (positionX + 125 >= screenWidth) positionY + 100 else positionY
                positionX = if (positionX + 125 >= screenWidth) 125 else positionX + 125
            }

            // Draw Dreadnoughts
            for (iterator in 0 until currentLevel.dreadnoghtCount) {
                enemySpaceShips.add(
                    EnemySpaceShip(
                        enemySpaceShipBitmaps[SpaceShipType.DREADNOUGHT]!!,
                        SpaceShipType.DREADNOUGHT,
                        positionX,
                        positionY,
                    )
                )
                positionY = if (positionX + 100 >= screenWidth) positionY + 100 else positionY
                positionX = if (positionX + 100 >= screenWidth) 100 else positionX + 100
            }
        }
        if (playerSpaceShip == null) {
            playerSpaceShip = PlayerSpaceShip(
                getBitmapResource(resources, R.drawable.player_spaceship)
            )
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        addGameObjects()

        gameThread.setRunning(true)
        updateThread.setRunning(true)

        if (gameThread.state == Thread.State.NEW || gameThread.state == Thread.State.TERMINATED) {
            gameThread.start()
            updateThread.start()
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        var retry = true
        while (retry) {
            try {
                gameThread.setRunning(false)
                gameThread.join()
                updateThread.setRunning(false)
                updateThread.join()
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