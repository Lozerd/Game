package com.example.entity

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.game.R


abstract class SpaceShip(image: Bitmap) : GameSprite(image) {
    protected var shotCooldown: Int = 20
    private var shotCooldownCounter = shotCooldown
    protected var spaceShipType: SpaceShipType = SpaceShipType.PLAYER
    private var spaceShipLife: Int = when (spaceShipType) {
        SpaceShipType.PLAYER -> 5
        SpaceShipType.CORVETTE -> 1
        SpaceShipType.INTERDICTOR -> 2
        SpaceShipType.VALIANT -> 3
        SpaceShipType.DREADNOUGHT -> 20
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
                this,
                this is PlayerSpaceShip
            )
        } else {
            shotCooldownCounter--
            null
        }
    }

    protected fun checkCollision(shot: Shot): Boolean {
        val collisionX: Boolean = shot.x + shot.w >= this.x || this.x + this.w >= shot.x
        val collisionY: Boolean = shot.y + shot.h >= this.y || this.y + this.h >= shot.y
        return collisionX && collisionY
    }

    // TODO test how life is decremented
    fun decrementLife() = spaceShipLife--

    protected fun isDestroyed(): Boolean {
        return spaceShipLife == 0
    }
}