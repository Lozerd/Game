package com.game.level

import android.graphics.Canvas

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
        else -> 0
    }

    init {
        corvetteCount = 3 * levelCoefficient
        interdictorCount = 2 * levelCoefficient
        valiantCount = if (id >= 3) levelCoefficient / 2 else 0
        dreadnoghtCount = if (id > 4) 1 else 0
    }
}