package com.example.entity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.Log
import com.example.game.R
import java.util.concurrent.ThreadLocalRandom

class Shot(image: Bitmap, ship: SpaceShip) : GameSprite(image) {
    init {
        x = ship.x
        y = ship.y
        yVelocity = 30 //ThreadLocalRandom.current().nextInt(20)
    }

    override fun update() {
        super.update()
        y += yVelocity
    }
}