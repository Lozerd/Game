package com.game.entity

import android.graphics.Bitmap

class PlayerSpaceShip(
    image: Bitmap,
    spaceShipType: SpaceShipType = SpaceShipType.PLAYER
) : SpaceShip(image, spaceShipType) {
    init {
        x = screenWidth / 2
        y = screenHeight - w * 2
    }
}
