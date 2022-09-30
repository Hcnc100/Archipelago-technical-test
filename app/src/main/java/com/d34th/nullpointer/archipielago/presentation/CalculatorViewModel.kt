package com.d34th.nullpointer.archipielago.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d34th.nullpointer.archipielago.R
import com.d34th.nullpointer.archipielago.core.states.Resource
import com.d34th.nullpointer.archipielago.domain.CalculatorRepository
import com.d34th.nullpointer.archipielago.models.Archipelago
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val calculatorRepository: CalculatorRepository
) : ViewModel() {


    private val _numberArchipelago =
        MutableStateFlow<Resource<List<List<Archipelago>>>>(Resource.Init)
    val numberArchipelago = _numberArchipelago.asStateFlow()

    private val _messageCalculator = Channel<Int>()
    val messageCalculator = _messageCalculator.receiveAsFlow()

    fun calculator(uriFile: Uri?) = viewModelScope.launch {
        if (uriFile != null) {
            _numberArchipelago.value = Resource.Loading
            try {
                val listArchipelago = withContext(Dispatchers.IO) {
                    calculatorRepository.calculatorArchipelago(uriFile)
                }
                _numberArchipelago.value = Resource.Success(listArchipelago)
            } catch (e: Exception) {
                Timber.e("Error calculator $e")
                _messageCalculator.trySend(R.string.error_calculating)
                _numberArchipelago.value = Resource.Init
            }
        } else {
            _messageCalculator.trySend(R.string.no_Select_file)
        }

    }


}