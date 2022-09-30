package com.d34th.nullpointer.archipielago.domain

import android.content.Context
import android.net.Uri
import com.d34th.nullpointer.archipielago.models.Archipelago
import com.d34th.nullpointer.archipielago.models.Edge
import com.d34th.nullpointer.archipielago.models.Island
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.io.BufferedReader
import java.io.InputStreamReader

class CalculatorRepoImpl(
    private val context: Context
) : CalculatorRepository {

    override suspend fun calculatorArchipelago(
        uriFile: Uri
    ): List<List<Archipelago>> = coroutineScope {
        val listLines = getAllLines(uriFile)
        var indexPointer = 0

        val numbersOfTest = listLines[indexPointer].toInt()
        indexPointer++


        val listAsyncTask = (1..numbersOfTest).map {
            val sizeOfIsland = listLines[indexPointer].toInt()
            indexPointer++

            val listCoordinatesTest =
                listLines.subList(indexPointer, indexPointer + sizeOfIsland)
            indexPointer += sizeOfIsland

            async { calculateArchipelago(listCoordinatesTest) }
        }

        listAsyncTask.awaitAll()
    }

    private fun calculateArchipelago(
        listCoordinatesIsland: List<String>
    ): List<Archipelago> {
        val listArchipelago = mutableSetOf<Archipelago>()
        val listIslands = getAllIsland(listCoordinatesIsland)

        // * add all archipelago and remove repeated
        listIslands.forEach { island ->
            listArchipelago.addAll(calculateArchipelagos(island, listIslands))
        }

        return listArchipelago.toList()
    }

    private fun getAllIsland(
        listCoordinates: List<String>
    ): List<Island> {
        // * convert all coordinates in Islands and deleter repeated elements
        return listCoordinates.map { text ->
            val coordinates = text.split(" ")
            Island(coordinates[0].toInt(), coordinates[1].toInt())
        }.toSet().toList()
    }


    private fun getAllLines(source: Uri): List<String> {
        return try {
            val inputStream = context.contentResolver.openInputStream(source)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val total = StringBuilder()
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                total.append(line).append('\n')
            }
            total.toString().split("\n").filter { it.isNotEmpty() }
        } catch (e: Exception) {
            return emptyList()
        }
    }


    private fun calculateArchipelagos(
        island: Island,
        listIsland: List<Island>
    ): List<Archipelago> {
        // * create set for no has repeated elements
        val listArch = mutableSetOf<Archipelago>()
        listIsland.asSequence().mapNotNull { currentIsland ->
            // * create all edges
            if (island != currentIsland) Edge(island, currentIsland) else null
        }.groupBy { it.distance }.asSequence().filter { it.value.size > 1 }.forEach { map ->
            val (_, listEdges) = map
            if (listEdges.size == 2) {
                // * if the list of edges only has 2 element so
                // * create one Archipelago
                listArch.add(Archipelago(listEdges[0], listEdges[1]))
            } else {
                // * if the list has more than 2 element so,
                // * calculate all combinations of edges
                listEdges.forEach { edge1 ->
                    listEdges.forEach { edge2 ->
                        // * create arch valid
                        // ! no has same edge
                        if (edge1 != edge2) {
                            listArch.add(Archipelago(edge1, edge2))
                        }
                    }
                }
            }
        }
        return listArch.toList()
    }
}