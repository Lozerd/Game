package com.example.entity

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.game.R


abstract class SpaceShip(image: Bitmap, spaceShipType: SpaceShipType) : GameSprite(image) {
    protected var shotCooldown: Int = 20
    private var shotCooldownCounter = shotCooldown
    protected var spaceShipType: SpaceShipType = SpaceShipType.PLAYER
    private var spaceShipLife: Int = 0

    init {
        yVelocity = when (spaceShipType) {
            SpaceShipType.PLAYER -> 45
            SpaceShipType.CORVETTE -> 5
            SpaceShipType.INTERDICTOR -> 10
            SpaceShipType.VALIANT -> 16
            SpaceShipType.DREADNOUGHT -> 20
        }
        spaceShipLife = when (spaceShipType) {
            SpaceShipType.PLAYER -> 5
            SpaceShipType.CORVETTE -> 1
            SpaceShipType.INTERDICTOR -> 2
            SpaceShipType.VALIANT -> 3
            SpaceShipType.DREADNOUGHT -> 20
        }
    }

    fun shoot(context: Context): Shot? {
        return if (shotCooldownCounter == 0) {
            shotCooldownCounter = shotCooldown
            Shot(
                BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.shot,
                    bitmapOptions
                ),
                this
            )
        } else {
            shotCooldownCounter--
            null
        }
    }

    fun hasCollision(shot: Shot): Boolean {
        return if (shot.ship == this) {
            false
        } else {
            this.x < shot.x + shot.w && this.x + this.w > shot.x && this.y < shot.y + shot.h && this.y + this.h > shot.y
        }

    }

    // TODO test how life is decremented
    fun decrementLife() = spaceShipLife--

    fun getLife() = spaceShipLife

    fun isDestroyed(): Boolean {
        return spaceShipLife == 0
    }
}