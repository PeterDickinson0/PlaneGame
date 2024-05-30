package com.example.flappybird

import android.content.Context

class Bullet(resID: Int, initialX: Int, initialY: Int, width: Int, length: Int, context: Context, damage: Int = 1, speed: Int = 30) : GameObject(resID, initialX, initialY, width, length, context)
{
    var damage: Int = 1
    var speed: Int = 30

    init {
        this.damage = damage
        this.speed = speed
    }

    fun fly()
    {
        hitbox.offset(0, -speed)
    }
}