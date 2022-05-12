package com.example.entity

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.Display
import com.example.game.R
import kotlin.random.Random

class EnemySpaceShip(context: Context): SpaceShip(context) {
    val velocity = 20 + Random.nextInt(100)
    init {
        this.image = BitmapFactory.decodeResource(context.resources, R.drawable.enemy_ship)
    }
}