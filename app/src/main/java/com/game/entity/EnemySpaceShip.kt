package com.game.entity

import android.graphics.Bitmap
import android.graphics.Canvas
import java.util.concurrent.ThreadLocalRandom

class EnemySpaceShip(image: Bitmap, spaceShipType: SpaceShipType) : SpaceShip(image, spaceShipType) {
    init {
        x = ThreadLocalRandom.current().nextInt(screenWidth - image.width)
        y = 0
    }

    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(image, x.toFloat(), y.toFloat(), null)
    }

    override fun update() {
        if (y < screenHeight - h) {
            y += yVelocity
        }
    }
}