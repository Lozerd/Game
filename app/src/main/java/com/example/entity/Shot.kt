package com.example.entity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.Log
import com.example.game.R
import java.util.concurrent.ThreadLocalRandom

open class Shot(image: Bitmap, val ship: SpaceShip) : GameSprite(image) {
    init {
        x = ship.x + ship.w / 2
        y = ship.y + h + 1
        yVelocity = if (this.ship is PlayerSpaceShip) -45 else 10
    }

    override fun update() {
        y += yVelocity
    }
}