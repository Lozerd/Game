package com.example.entity

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import com.example.game.R
import java.util.concurrent.ThreadLocalRandom

class EnemySpaceShip(image: Bitmap) : SpaceShip(image) {
    var shotCooldown: Int

    init {
        x = ThreadLocalRandom.current().nextInt(screenWidth - image.width)
        y = 0
        yVelocity = 10 // 20 + ThreadLocalRandom.current().nextInt(5, 25)
        shotCooldown = 20
    }

    fun shoot(context: Context): Shot? {
        if (shotCooldown == 0) {
            shotCooldown = 20
            return Shot(
                BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.shot
                ),
                this
            )
        } else {
            shotCooldown -= 1
            return null
        }
    }

    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(image, x.toFloat(), y.toFloat(), null)
    }

    override fun update() {
        /*
            Move EnemySpaceShip down by yVelocity
         */
        if (y < screenHeight + h) {
            y += yVelocity
        }
    }
}