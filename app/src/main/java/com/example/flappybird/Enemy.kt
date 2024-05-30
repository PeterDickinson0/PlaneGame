package com.example.flappybird

import android.content.Context

class Enemy(resID: Int, initialX: Int, initialY: Int, width: Int, length: Int, context: Context, coolDown: Int, health: Int = 3) : Plane(resID, initialX, initialY, width, length, context, coolDown)
{
    var moveSpeed = 7
    var movingRight = true
    var health = 0

    init
    {
        this.health = health
    }

    fun weave()
    {
        if (movingRight)
        {
            hitbox.offset(moveSpeed, 0)
            if (hitbox.centerX() >= 900)
            {
                movingRight = false
            }
        }
        else
        {
            hitbox.offset(moveSpeed * -1, 0)
            if (hitbox.centerX() <= 100)
            {
                movingRight = true
            }
        }

    }

    fun damage(damage: Int)
    {
        health-=damage
    }
}