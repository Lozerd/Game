package com.game.level

import android.content.Context
import android.graphics.Bitmap
import com.game.R
import com.game.entity.SpaceShip
import com.game.entity.SpaceShipType

class GameMap(context: Context, private val screenWidth: Int) {

    private val enemySpaceShipBitmaps: Map<SpaceShipType, Bitmap> = mapOf(
        SpaceShipType.CORVETTE to SpaceShip.getBitmapResource(
            context.resources,
            R.drawable.corvette
        ),
        SpaceShipType.PLAYER to SpaceShip.getBitmapResource(
            context.resources,
            R.drawable.player_spaceship
        ),
        SpaceShipType.INTERDICTOR to SpaceShip.getBitmapResource(
            context.resources,
            R.drawable.interdictor
        ),
        SpaceShipType.VALIANT to SpaceShip.getBitmapResource(context.resources, R.drawable.valiant),
        SpaceShipType.DREADNOUGHT to SpaceShip.getBitmapResource(
            context.resources,
            R.drawable.dreadnought
        )
    )

    fun generateMap(gameLevel: GameLevel): ArrayList<String> {
        val levelMap = arrayListOf<String>()
        var currentSpaceShipSymbol = ""
        val spaceShipTypes = SpaceShipType.values().filter { it != SpaceShipType.PLAYER }

        var spaceShipRowCount = 0
        val spaceShipRowMaxCount = getMaxShipInRowCount(spaceShipTypes.first())

        val currentRow: Array<String> = Array(spaceShipRowMaxCount) { "#" }

        for (spaceShipType in spaceShipTypes) {
            currentSpaceShipSymbol = getSpaceShipSymbol(spaceShipType)
            spaceShipRowCount = gameLevel.shipCountMap[spaceShipType]!!
            if (spaceShipRowCount != 0) {
                spaceShipRowCount = getMaxShipInRowCount(spaceShipType) / spaceShipRowCount
                for (counter in 0..spaceShipRowCount) {
                    currentRow[(spaceShipRowMaxCount - counter - 1) / 2] = currentSpaceShipSymbol
                }
                levelMap.add(currentRow.joinToString(""))
                currentRow.fill("#")
            }
        }
        return levelMap
    }

    private fun getMaxShipInRowCount(spaceShipType: SpaceShipType): Int = when (spaceShipType) {
        SpaceShipType.PLAYER -> screenWidth / 2
        SpaceShipType.CORVETTE -> screenWidth / enemySpaceShipBitmaps[SpaceShipType.CORVETTE]!!.width  // 50
        SpaceShipType.INTERDICTOR -> screenWidth / enemySpaceShipBitmaps[SpaceShipType.INTERDICTOR]!!.width // 80
        SpaceShipType.VALIANT -> screenWidth / enemySpaceShipBitmaps[SpaceShipType.VALIANT]!!.width // 100
        SpaceShipType.DREADNOUGHT -> screenWidth / enemySpaceShipBitmaps[SpaceShipType.DREADNOUGHT]!!.width // 150
    }

    private fun getSpaceShipSymbol(spaceShipType: SpaceShipType): String = when (spaceShipType) {
        /* Every SpaceShip must have symbol, to be drawn onto map */
        SpaceShipType.PLAYER -> "0"
        SpaceShipType.CORVETTE -> "1"
        SpaceShipType.INTERDICTOR -> "2"
        SpaceShipType.VALIANT -> "3"
        SpaceShipType.DREADNOUGHT -> "4"
    }
}