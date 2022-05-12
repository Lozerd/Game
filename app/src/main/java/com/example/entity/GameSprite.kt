package com.example.entity

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas

abstract class GameSprite(var image: Bitmap) {
    var x: Int = 0
    var y: Int = 0
    var w: Int = 0
    var h: Int = 0
    protected var xVelocity = 0
    protected  var yVelocity = 0
    protected val screenWidth = Resources.getSystem().displayMetrics.widthPixels
    protected val screenHeight = Resources.getSystem().displayMetrics.heightPixels

    init {
        w = image.width
        h = image.height
        x = screenWidth / 2
        y = screenWidth / 2
    }

    open fun draw(canvas: Canvas) = canvas.drawBitmap(image, x.toFloat(), y.toFloat(), null)

    open fun update() {
        throw NotImplementedError("Must be implemented in subclasses")
    }
}