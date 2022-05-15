package com.game.entity

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas

abstract class GameSprite(var image: Bitmap) {
    companion object {
        val bitmapOptions: BitmapFactory.Options
            get() {
                val b = BitmapFactory.Options()
                b.inScaled = false
                return b
            }
    }
    protected var xVelocity = 0
    protected var yVelocity = 0
    protected val screenWidth = Resources.getSystem().displayMetrics.widthPixels
    protected val screenHeight = Resources.getSystem().displayMetrics.heightPixels
    var x: Int = screenWidth / 2
    var y: Int = screenHeight / 2
    var w: Int = image.width
    var h: Int = image.height

    open fun draw(canvas: Canvas) = canvas.drawBitmap(image, x.toFloat(), y.toFloat(), null)

    open fun update() {

    }
}