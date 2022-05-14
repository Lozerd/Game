package com.example.entity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.Log
import com.example.game.R
import java.util.concurrent.ThreadLocalRandom

class Shot(image: Bitmap, ship: SpaceShip, val isPlayerShot: Boolean) : GameSprite(image) {
    init {
        x = ship.x + ship.w / 2
        y = ship.y
        yVelocity = if (isPlayerShot) -45 else 30
    }

    override fun update() {
        y += yVelocity
    }
}