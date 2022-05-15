package com.game.core

import android.graphics.Canvas
import android.view.SurfaceHolder

class UpdateThread(private val game: GameView) : Thread() {
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
            startTime = System.currentTimeMillis()
            this.game.update()

            timeMillis = (System.currentTimeMillis() - startTime)
            waitTime = targetTime - timeMillis

            try {
                if (waitTime > 0) {
                    sleep(waitTime)
                } else {
                    sleep(10)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setRunning(isRunning: Boolean) {
        running = isRunning
    }
}