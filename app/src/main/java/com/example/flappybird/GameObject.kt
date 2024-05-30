package com.example.flappybird

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect

open class GameObject(resID: Int, initialX: Int, initialY: Int, width: Int, length: Int, context: Context)
{
    lateinit var hitbox : Rect
    lateinit var sprite : Bitmap
    private lateinit var options: BitmapFactory.Options
    private lateinit var context: Context

    init {
        hitbox = Rect(initialX, initialY, initialX+width, initialY+length)
        options = BitmapFactory.Options()
        options.inScaled = false
        sprite = BitmapFactory.decodeResource(context.resources, resID, options)
        this.context = context
    }

    fun updateSprite(resID: Int)
    {
        sprite = BitmapFactory.decodeResource(context.resources, resID, options)
    }

}