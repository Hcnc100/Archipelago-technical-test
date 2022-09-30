package com.d34th.nullpointer.archipielago.ui.states

import android.content.Context
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

class MainScreenState(
    context: Context,
    scaffoldState: ScaffoldState,
    private val launcherSelectFile: ManagedActivityResultLauncher<Array<String>, Uri?>
) : SimpleScreenState(scaffoldState, context) {

    fun launchSelectFile() {
        launcherSelectFile.launch(arrayOf("text/plain"))
    }
}


@Composable
fun rememberMainScreenState(
    actionSelectFile: (Uri?) -> Unit,
    context: Context = LocalContext.current,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    launcherSelectFile: ManagedActivityResultLauncher<Array<String>, Uri?> = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument(),
        actionSelectFile
    )
) = remember(scaffoldState, launcherSelectFile) {
    MainScreenState(context, scaffoldState, launcherSelectFile)
}