package com.d34th.nullpointer.archipielago.domain

import android.net.Uri
import com.d34th.nullpointer.archipielago.models.Archipelago

interface CalculatorRepository {
    suspend fun calculatorArchipelago(uriFile: Uri): List<List<Archipelago>>
}