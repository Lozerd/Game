package com.example.entity

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import com.example.game.R
import java.util.concurrent.ThreadLocalRandom

class EnemySpaceShip(image: Bitmap) : SpaceShip(image) {

    init {
        spaceShipType = SpaceShipType.VALIANT
        x = ThreadLocalRandom.current().nextInt(screenWidth - image.width)
        y = 0
        yVelocity = 5 + ThreadLocalRandom.current().nextInt(1, 10)
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