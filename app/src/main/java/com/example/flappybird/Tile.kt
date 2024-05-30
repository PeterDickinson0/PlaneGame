package com.example.flappybird

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log

class Tile(sprite: Bitmap, tileID: Int)
{
    private lateinit var attributes : List<List<Int>>
    //left middle right or top middle bottom
    //top, right, bottom, left
    lateinit var sprite: Bitmap
    lateinit var topMatches: MutableList<Tile>
    lateinit var rightMatches: MutableList<Tile>
    var id: Int = 0

    init
    {
        this.id = tileID
        this.sprite = sprite
        var top = listOf<Int>(sprite.getPixel(0,0), sprite.getPixel(7, 0), sprite.getPixel(15, 0))
        var right = listOf<Int>(sprite.getPixel(15,0), sprite.getPixel(15, 7), sprite.getPixel(15, 15))
        var bottom = listOf<Int>(sprite.getPixel(15,15), sprite.getPixel(7, 15), sprite.getPixel(0, 15))
        var left = listOf<Int>(sprite.getPixel(0,0), sprite.getPixel(0, 7), sprite.getPixel(0, 15))
        attributes = listOf(top, right, bottom, left)
    }

    fun determineMatches(tiles: List<Tile>)
    {
        topMatches = mutableListOf<Tile>()
        rightMatches = mutableListOf<Tile>()
        for (tile in tiles)
        {
            var worksTop = true
            var worksRight = true
            for (i in 0..2)
            {
                if (tile.attributes[2][i] != this.attributes[0][i])
                {
                    if (tile.attributes[2][i] != -13544368 && this.attributes[0][i] != -13544368)
                    {
                        worksTop = false
                    }
                }
                if (tile.attributes[3][i] != this.attributes[1][i])
                {
                    if (tile.attributes[3][i] != -13544368 && this.attributes[1][i] != -13544368)
                    {
                        worksRight = false
                    }
                }
            }
            if (worksTop)
            {
                topMatches.add(tile)
            }
            if (worksRight)
            {
                rightMatches.add(tile)
            }
        }
    }

    override fun toString(): String
    {
        return id.toString()
    }
}