package com.example.core

import android.graphics.Canvas
import android.view.SurfaceHolder
import com.example.startup.GameView

class GameThread(private val surfaceHolder: SurfaceHolder, private val game: GameView) : Thread() {
    companion object {
        private var canvas: Canvas? = null
    }

    private var running: Boolean = false
    private val targetFPS = 60

    override fun run() {
        var startTime: Long
        var timeMillis: Long
        var waitTime: Long
        val targetTime = (1000 / targetFPS).toLong()

        while (running) {
            startTime = System.nanoTime()
            canvas = null

            try {
                canvas = this.surfaceHolder.lockCanvas()
                synchronized(surfaceHolder) {
                    this.game.update()
                    this.game.draw(canvas!!)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000
            waitTime = targetTime - timeMillis

            if (waitTime < 0) {
                waitTime = 0
            }
            try {
                sleep(waitTime)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setRunning(isRunning: Boolean) {
        running = isRunning
    }
}