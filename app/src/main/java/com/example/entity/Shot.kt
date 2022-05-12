package com.example.entity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import com.example.game.R
import java.util.concurrent.ThreadLocalRandom

class Shot(image: Bitmap, ship: SpaceShip) : GameSprite(image) {
    init {
        x = ship.w / 2
        y = ship.y / 2
        yVelocity = ThreadLocalRandom.current().nextInt(40)
    }

    override fun update() {
        super.update()
        y += yVelocity
    }
}