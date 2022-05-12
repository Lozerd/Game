package com.example.entity

import android.graphics.Bitmap
import android.graphics.Canvas
import java.util.concurrent.ThreadLocalRandom

class EnemySpaceShip(image: Bitmap): SpaceShip(image) {
    init {
        x = ThreadLocalRandom.current().nextInt(screenWidth - image.width)
        y = 0
        yVelocity = 20 + ThreadLocalRandom.current().nextInt(40)
    }

    fun shoot(){

    }

    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(image, x.toFloat(), y.toFloat(), null)
    }

    override fun update() {
        /*
            Move EnemySpaceShip down by yVelocity
         */
        y += yVelocity
    }
}