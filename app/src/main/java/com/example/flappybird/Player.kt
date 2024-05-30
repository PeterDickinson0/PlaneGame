package com.example.flappybird

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import com.example.flappybird.MainActivity
import org.chromium.base.ContextUtils.getApplicationContext

class Player(resID: Int, initialX: Int, initialY: Int, width: Int, length: Int, context: Context, coolDown: Int) : Plane(resID, initialX, initialY, width, length, context, coolDown)
{
    var followSpeed: Double = .2
    var level = 0

    fun followMouse(mouseX: Int)
    {
        val dist = mouseX - hitbox.centerX()
        hitbox.offset((dist * followSpeed).toInt(), 0)
    }

    fun upgrade()
    {
        level++
        coolDown-=15
    }

    fun damage()
    {
        level--
        coolDown+=15
    }

}