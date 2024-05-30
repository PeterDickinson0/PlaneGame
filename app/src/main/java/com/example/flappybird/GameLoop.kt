package com.example.flappybird

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.util.Log
import java.util.Random

class GameLoop(gamePanel: GamePanel) : Runnable
{

    private lateinit var thisPanel: GamePanel

    private lateinit var gameThread: Thread

    lateinit var player: Player

    lateinit var playerBullets: MutableList<Bullet>

    lateinit var enemyBullets: MutableList<Bullet>

    lateinit var enemies: MutableList<Enemy>

    lateinit var upgrades: MutableList<GameObject>

    var spawnHeights = mutableListOf(300, 550, 800, 1050, 1100)

    var playerLevelSprite = listOf(R.drawable.ship_0009, R.drawable.ship_0005, R.drawable.ship_0001)

    var enemySprites = listOf(R.drawable.ship_0012, R.drawable.ship_0013, R.drawable.ship_0014, R.drawable.ship_0015, R.drawable.ship_0016, R.drawable.ship_0017, R.drawable.ship_0018, R.drawable.ship_0019, R.drawable.ship_0020, R.drawable.ship_0021, R.drawable.ship_0022, R.drawable.ship_0023)

    var spotsTaken: MutableList<Boolean> = mutableListOf(false, false, false, false, false)

    lateinit var planes: MutableList<Plane>

    init
    {
        thisPanel = gamePanel
        enemies = ArrayList()
        planes = ArrayList()
        upgrades = ArrayList()
        player = Player(R.drawable.ship_0009, 470, 1825, 250, 250, gamePanel.context, 40)
        enemies.add(createRandomEnemy(3))
        spawnHeights.removeAt(3)
        planes.add(player)
        planes.addAll(enemies)
        playerBullets = ArrayList()
        enemyBullets = ArrayList()
        gameThread = Thread(this)
        startGameLoop()
    }

    override fun run() {
        while (true)
        {
            val randEnemySpawn = (Math.random() * (500 - player.level*100)).toInt()
            if (randEnemySpawn == 0 && spawnHeights.size > 0)
            {
                val height = spawnHeights.random()
                enemies.add(createRandomEnemy(height))
                spawnHeights.remove(height)
            }

            player.followMouse(thisPanel.fingerX)

            if (player.currentCoolDown == 0)
            {
                player.fire()
                playerBullets.add(Bullet(R.drawable.tile_0000, player.hitbox.centerX(), player.hitbox.top, 100, 100, thisPanel.context, player.level + 1, 50))
                playerBullets.add(Bullet(R.drawable.tile_0000, player.hitbox.centerX() - 100, player.hitbox.top, 100, 100, thisPanel.context, player.level + 1, 50))
            }

            for (enemy in enemies)
            {
                enemy.weave()
                if (enemy.currentCoolDown == 0)
                {
                    enemy.fire()
                    enemyBullets.add(Bullet(R.drawable.tile_0000, enemy.hitbox.centerX(), enemy.hitbox.top + 100, 100, 100, thisPanel.context, 1, -60))
                    enemyBullets.add(Bullet(R.drawable.tile_0000, enemy.hitbox.centerX() - 100, enemy.hitbox.top + 100, 100, 100, thisPanel.context, 1, -60))
                }
                enemy.update()
            }

            for (bullet in playerBullets.union(enemyBullets))
            {
                bullet.fly()
            }

            enemyLoop@ for (enemyIndex in enemies.size - 1 downTo 0)
            {
                for (bulletIndex in playerBullets.size - 1 downTo 0)
                {
                    if (Rect.intersects(playerBullets[bulletIndex].hitbox, enemies[enemyIndex].hitbox))
                    {
                        enemies[enemyIndex].damage(playerBullets[bulletIndex].damage)
                        if (enemies[enemyIndex].health <= 0)
                        {
                            spawnHeights.add(enemies[enemyIndex].hitbox.top)
                            playerBullets.remove(playerBullets[bulletIndex])
                            upgrades.add(GameObject(R.drawable.tile_0025, enemies[enemyIndex].hitbox.centerX(), enemies[enemyIndex].hitbox.centerY(), 100, 100, thisPanel.context))
                            enemies.removeAt(enemyIndex)
                            continue@enemyLoop // make sure a bullet doesnt check collisions with a non-existant enemy
                        }
                        playerBullets.remove(playerBullets[bulletIndex])
                    }
                    else if (playerBullets[bulletIndex].hitbox.top > 2000)
                    {
                        playerBullets.remove(playerBullets[bulletIndex])
                    }
                }
            }

            for (upgrade in upgrades)
            {
                upgrade.hitbox.offset(0, 5)
            }

            for (upgradeIndex in upgrades.size - 1 downTo 0)
            {
                if (Rect.intersects(player.hitbox, upgrades[upgradeIndex].hitbox))
                {
                    upgrades.removeAt(upgradeIndex)
                    if (player.level < 2)
                    {
                        player.upgrade()
                        player.updateSprite(playerLevelSprite[player.level])
                    }
                }
            }

            for (bulletIndex in enemyBullets.size - 1 downTo 0)
            {
                if (Rect.intersects(enemyBullets[bulletIndex].hitbox, player.hitbox))
                {
                    enemyBullets.remove(enemyBullets[bulletIndex])
                    if (player.level == 0)
                    {
                        endGame()
                    }
                    else
                    {
                        player.damage()
                        player.updateSprite(playerLevelSprite[player.level])
                    }
                }
                else if (enemyBullets[bulletIndex].hitbox.top < -200)
                {
                    enemyBullets.remove(enemyBullets[bulletIndex])
                }
            }
            player.update()

            thisPanel.render()
        }
    }

    fun startGameLoop()
    {
        gameThread.start()
    }

    fun createRandomEnemy(height: Int) : Enemy
    {
        val seed = Math.random()
        return Enemy(enemySprites[(seed * enemySprites.size).toInt()], (seed *  1000).toInt(), height, 250, 250, thisPanel.context, (seed * 5).toInt() + 20, 5)
    }

    fun endGame()
    {
        var info : Intent = Intent(thisPanel.context, StartActivity::class.java)
        thisPanel.context.startActivity(info)
    }


}