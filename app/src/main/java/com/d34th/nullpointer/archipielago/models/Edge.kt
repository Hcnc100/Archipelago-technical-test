package com.d34th.nullpointer.archipielago.models

import kotlin.math.pow
import kotlin.math.sqrt

data class Edge(
    val islandX: Island,
    val islandY: Island
) {
    val distance: Float

    init {
        val sumDistance =
            (islandX.x - islandY.x).toFloat().pow(2) + (islandX.y - islandY.y).toFloat().pow(2)
        distance = sqrt(sumDistance)
    }

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            javaClass != other?.javaClass -> false
            else -> {
                other as Edge
                when {
                    (islandX == other.islandX) && (islandY == other.islandY) -> true
                    (islandX == other.islandY) && (islandY == other.islandX) -> true
                    else -> false
                }
            }
        }
    }

    override fun hashCode(): Int {
        var result = islandX.hashCode() + islandY.hashCode()
        result *= 31
        return result
    }

}