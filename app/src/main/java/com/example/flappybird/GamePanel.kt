package com.example.flappybird

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class GamePanel(context: Context) : SurfaceView(context), SurfaceHolder.Callback
{

    private lateinit var clouds : Paint

    private lateinit var debug : Paint

    private lateinit var gameLoop: GameLoop

    private lateinit var tiles: MutableList<Tile>

    var fingerX: Int = 0

    var fingerY: Int = 0

    lateinit var background : ArrayList<Array<Tile>>

    var backgroundOffset: Int = 0

    init {
        holder.addCallback(this)

        debug = Paint()
        debug.color = Color.GREEN
        clouds = Paint()
        clouds.setARGB(55, 240, 250, 255)

        initTiles()
        background = createTestBackground(15)

        gameLoop = GameLoop(this)
    }

    fun render()
    {
        var c : Canvas? = holder.lockCanvas()

        if (c != null)
        {
            c.drawColor(Color.BLACK)

            for (row in 0..12)
            {
                for (col in 0..5)
                {
                    c.drawBitmap(background[row][col].sprite, null, Rect(180*col, 180*(row-1) + backgroundOffset, 180*col + 180, 180*row+ backgroundOffset), null)
                }
            }
            backgroundOffset+=5
            if (backgroundOffset == 180)
            {
                backgroundOffset = 0
                updateBackground()
            }

            c.drawColor(clouds.color)

            for (enemy in gameLoop.enemies)
            {
                c.drawRect(enemy.hitbox, debug)
                c.drawBitmap(enemy.sprite, null, enemy.hitbox, null)
            }

            for (upgrade in gameLoop.upgrades)
            {
                c.drawBitmap(upgrade.sprite, null, upgrade.hitbox, null)
            }

            c.drawRect(gameLoop.player.hitbox, debug)
            c.drawBitmap(gameLoop.player.sprite, null, gameLoop.player.hitbox, null)

            for (bullet in gameLoop.playerBullets.union(gameLoop.enemyBullets))
            {
                c.drawRect(bullet.hitbox, debug)
                c.drawBitmap(bullet.sprite, null, bullet.hitbox, null)
            }

            holder.unlockCanvasAndPost(c)
        }
    }

    fun update()
    {

    }

    private fun initTiles()
    {
        tiles = mutableListOf<Tile>()
        var options = BitmapFactory.Options()
        options.inScaled = false
        var rawTiles = BitmapFactory.decodeResource(context.resources, R.drawable.tiles, options)
        var xCorner = 0
        var yCorner = 51
        var id = 0
        for (r in 1..7)
        {
            for (c in 1..6)
            {
                var badTiles = arrayOf(2, 3, 4, 5, 6, 8, 9, 10, 11, 12, 14, 15, 16, 17, 18)
                if (!badTiles.contains(id))
                {
                    tiles.add(Tile(Bitmap.createBitmap(rawTiles, xCorner, yCorner, 16, 16), id))
                }
                id++
                xCorner += 17
            }
            xCorner = 0
            yCorner += 17
        }
        for (tile in tiles)
        {
            tile.determineMatches(tiles)
        }
    }

    fun updateBackground()
    {
        background.removeLast()
        var newRow: Array<Tile> = Array(6) {tiles[0]}
        for (c in 0..5)
        {
            if (c == 0)
            {
                if (background[0][0].topMatches.isEmpty())
                {
                    newRow[c] = tiles[5]
                }
                else
                {
                    newRow[c] = background[0][0].topMatches.random()
                }
            }
            else
            {
                if (newRow[c-1].rightMatches.intersect(background[0][c].topMatches.toSet()).isEmpty())
                {
                    newRow[c] = newRow[c-1].rightMatches.random()
                }
                else
                {
                    newRow[c] = newRow[c-1].rightMatches.intersect(background[0][c].topMatches.toSet()).random()
                }
            }
        }
        background.add(0, newRow)
    }

    fun createTestBackground(seed: Int) : ArrayList<Array<Tile>>
    {
        var map = Array(13) {Array(6) {tiles[0]} }
        map[12][0] = tiles[seed]
        for (r in 12 downTo 0)
        {
            for (c in 0..5)
            {
                if (!(r == 12 && c == 0))
                {
                    if (c == 0)
                    {
                        if (map[r+1][c].topMatches.isEmpty())
                        {
                            map[r][c] = tiles[5]
                        }
                        else
                        {
                            map[r][c] = map[r + 1][c].topMatches.random()
                        }
                    }
                    else if (r == 12)
                    {
                        if (map[r][c-1].rightMatches.isEmpty())
                        {
                            map[r][c] = tiles[5]
                        }
                        else
                        {
                            map[r][c] = map[r][c - 1].rightMatches.random()
                        }
                    }
                    else
                    {
                        if (map[r][c-1].rightMatches.intersect(map[r+1][c].topMatches.toSet()).isEmpty())
                        {
                            map[r][c] = map[r][c-1].rightMatches.random()
                        }
                        else
                        {
                            map[r][c] = map[r][c-1].rightMatches.intersect(map[r+1][c].topMatches.toSet()).random()
                        }
                    }
                }
            }
        }
        var mapArray: ArrayList<Array<Tile>> = ArrayList()
        for (row in map)
        {
            mapArray.add(row)
        }
        return mapArray
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean
    {
        if (event != null)
        {
            fingerX = event.x.toInt()
            fingerY = event.y.toInt()
        }
        return true
    }

    override fun surfaceCreated(surfaceHolder : SurfaceHolder) {
        Log.d("PETER", "surface")
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        Log.d("PETER", "surfaceChanged: 1")
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        Log.d("PETER", "surfaceChanged: 1")
    }

}