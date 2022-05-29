package com.game.level

import com.game.R
import com.game.entity.SpaceShip
import com.game.entity.SpaceShipType

class GameLevel(val id: Int) {
    var corvetteCount: Int
    var interdictorCount: Int
    var valiantCount: Int
    var dreadnoghtCount: Int
    private val levelCoefficient: Int = when (id) {
        1 -> 1
        2 -> 2
        3 -> 3
        4 -> 4
        5 -> 6
        else -> 6
    }
    val shipCountMap: Map<SpaceShipType, Int> = mapOf(
        SpaceShipType.PLAYER to 1,
        SpaceShipType.CORVETTE to 3 * levelCoefficient,
        SpaceShipType.INTERDICTOR to 2 * levelCoefficient,
        SpaceShipType.VALIANT to if (id >= 3) levelCoefficient / 2 else 0,
        SpaceShipType.DREADNOUGHT to if (id > 4) 1 else 0
    )

    // TODO to remove
    init {
        corvetteCount = 3 * levelCoefficient
        interdictorCount = 2 * levelCoefficient
        valiantCount = if (id >= 3) levelCoefficient / 2 else 0
        dreadnoghtCount = if (id > 4) 1 else 0
    }
}