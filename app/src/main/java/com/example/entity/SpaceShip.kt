package com.example.entity

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.DisplayMetrics
import com.example.game.R
import kotlin.random.Random


abstract class SpaceShip(context: Context) {
    protected open var image: Bitmap = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.blank_ship
    )
    var x = (Random.nextInt(context.resources.displayMetrics.widthPixels - 2 * getWidth()))
    var y = 0


    fun getWidth(): Int = image.width

    fun getHeight(): Int = image.height

    fun getShipBitmap() = image

}