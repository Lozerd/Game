package com.example.entity

import android.graphics.Bitmap

class PlayerSpaceShip(image: Bitmap): SpaceShip(image) {
    val life = 5
    init {
        x = screenWidth / 2
        y = screenHeight / 2
        xVelocity = 50
        yVelocity = 50
    }
//    val image: Bitmap? = BitmapFactory.decodeResource(context.resources, R.drawable.player_ship)
}
