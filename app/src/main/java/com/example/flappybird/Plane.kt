package com.example.flappybird

import android.content.Context

open class Plane (resID: Int, initialX: Int, initialY: Int, width: Int, length: Int, context: Context, coolDown: Int = 50) : GameObject(resID, initialX, initialY, width, length, context)
{
    var coolDown: Int = 50
    var currentCoolDown: Int = 0

    init
    {
        this.coolDown = coolDown
    }

    fun update()
    {
        currentCoolDown--
    }

    fun fire()
    {
        currentCoolDown = coolDown
    }
}