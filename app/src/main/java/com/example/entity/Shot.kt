package com.example.entity

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.game.R

class Shot(context: Context, x: Float, y: Float) {
    val image: Bitmap = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.shot
    )
}