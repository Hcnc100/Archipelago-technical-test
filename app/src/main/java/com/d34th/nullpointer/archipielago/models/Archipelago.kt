package com.d34th.nullpointer.archipielago.models

data class Archipelago(
    val edge1: Edge,
    val edge2: Edge
) {
    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            javaClass != other?.javaClass -> return false
            else -> {
                other as Archipelago
                when {
                    edge1 == other.edge1 && edge2 == other.edge2 -> true
                    edge1 == other.edge2 && edge2 == other.edge1 -> true
                    else -> false
                }
            }
        }
    }

    override fun hashCode(): Int {
        var result = edge1.hashCode() + edge2.hashCode()
        result *= 31
        return result
    }

}