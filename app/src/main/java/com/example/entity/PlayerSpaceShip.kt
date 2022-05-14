package com.example.entity

import android.graphics.Bitmap

class PlayerSpaceShip(image: Bitmap): SpaceShip(image) {
    init {
        spaceShipType = SpaceShipType.PLAYER
        x = screenWidth / 2
        y = screenHeight - w * 2
    }
}
