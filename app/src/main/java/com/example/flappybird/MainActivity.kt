package com.example.flappybird

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    private lateinit var gameContext : Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameContext = this
        setContentView(GamePanel(this))
    }

    fun getGameContext() : Context
    {
        return gameContext
    }
}