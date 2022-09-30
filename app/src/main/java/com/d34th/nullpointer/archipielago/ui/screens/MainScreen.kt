package com.d34th.nullpointer.archipielago.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.d34th.nullpointer.archipielago.R
import com.d34th.nullpointer.archipielago.core.states.Resource
import com.d34th.nullpointer.archipielago.models.Archipelago
import com.d34th.nullpointer.archipielago.presentation.CalculatorViewModel
import com.d34th.nullpointer.archipielago.ui.states.MainScreenState
import com.d34th.nullpointer.archipielago.ui.states.rememberMainScreenState

@Composable
fun MainScreen(
    calculatorViewModel: CalculatorViewModel = hiltViewModel(),
    mainScreenState: MainScreenState = rememberMainScreenState(actionSelectFile = calculatorViewModel::calculator)
) {
    val stateCalculator by calculatorViewModel.numberArchipelago.collectAsState()
    LaunchedEffect(key1 = Unit) {
        calculatorViewModel.messageCalculator.collect(mainScreenState::showSnackMessage)
    }

    MainScreen(
        stateCalculation = stateCalculator,
        scaffoldState = mainScreenState.scaffoldState,
        actionSelectFile = mainScreenState::launchSelectFile
    )

}

@Composable
private fun MainScreen(
    scaffoldState: ScaffoldState,
    actionSelectFile: () -> Unit,
    stateCalculation: Resource<List<List<Archipelago>>>
) {

    val titleScreen = remember {
        when (stateCalculation) {
            is Resource.Success -> R.string.title_result
            Resource.Loading -> R.string.text_calculation
            Resource.Failure, Resource.Init -> R.string.title_select_file
        }
    }


    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            SelectButton(
                actionSelectFile = actionSelectFile,
                stateCalculation = stateCalculation
            )
        }
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(titleScreen),
                style = MaterialTheme.typography.body1
            )
            if (stateCalculation is Resource.Success) {
                Spacer(modifier = Modifier.size(10.dp))
                repeat(stateCalculation.data.size) {
                    Text(
                        style = MaterialTheme.typography.body1,
                        text = stringResource(
                            R.string.text_result_archipelagos,
                            it + 1, stateCalculation.data[it].size
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun SelectButton(
    actionSelectFile: () -> Unit,
    stateCalculation: Resource<Any>
) {
    when (stateCalculation) {
        Resource.Loading -> CircularProgressIndicator()
        Resource.Failure, Resource.Init -> {
            Button(onClick = actionSelectFile) {
                Text(text = stringResource(R.string.text_select_file))
            }
        }
        is Resource.Success -> {
            Button(onClick = actionSelectFile) {
                Text(text = stringResource(R.string.text_select_another_file))
            }
        }
    }
}