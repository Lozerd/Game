package com.game.entity

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.game.R


abstract class SpaceShip(
    image: Bitmap,
    val spaceShipType: SpaceShipType
) : GameSprite(image) {
    private var spaceShipLife: Int = 0
    private var shotCooldown: Int
    private var shotCooldownCounter: Int

    companion object {
        fun getBitmapResource(resources: Resources, param: Int): Bitmap {
            return BitmapFactory.decodeResource(resources, param, bitmapOptions)
        }
    }


    init {
        yVelocity = when (spaceShipType) {
            SpaceShipType.PLAYER -> 45
            SpaceShipType.CORVETTE -> 1
            SpaceShipType.INTERDICTOR -> 2
            SpaceShipType.VALIANT -> 3
            SpaceShipType.DREADNOUGHT -> 4
        }
        spaceShipLife = when (spaceShipType) {
            SpaceShipType.PLAYER -> 20
            SpaceShipType.CORVETTE -> 1
            SpaceShipType.INTERDICTOR -> 2
            SpaceShipType.VALIANT -> 3
            SpaceShipType.DREADNOUGHT -> 10
        }
        shotCooldown = when (spaceShipType) {
            SpaceShipType.PLAYER -> 10
            SpaceShipType.CORVETTE -> 80
            SpaceShipType.INTERDICTOR -> 70
            SpaceShipType.VALIANT -> 60
            SpaceShipType.DREADNOUGHT -> 50
        }
        shotCooldownCounter = shotCooldown
    }


    fun shoot(context: Context): Shot? {
        return if (shotCooldownCounter == 0) {
            shotCooldownCounter = shotCooldown
            Shot(
                getBitmapResource(context.resources, R.drawable.shot),
                this
            )
        } else {
            shotCooldownCounter--
            null
        }
    }

    fun hasCollision(shot: Shot): Boolean {
        return if (shot.ship == this || (this is EnemySpaceShip && shot.ship is EnemySpaceShip)) {
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