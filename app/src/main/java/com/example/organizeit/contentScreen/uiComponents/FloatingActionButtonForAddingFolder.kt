package com.example.organizeit.contentScreen.uiComponents


import android.content.Context
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.organizeit.ContentReceivedFromOutsideApp
import com.example.organizeit.contentScreen.ContentScreenViewModel
import kotlinx.coroutines.launch

@Composable
fun FloatingActionButtonViewForAddingFolder(){
    val viewModel: ContentScreenViewModel = hiltViewModel()
    val scope= rememberCoroutineScope()
    val context:Context= LocalContext.current
    val isCRFOAvailable=ContentReceivedFromOutsideApp.isCRFOAvailable.collectAsState()
    Log.d("haha",isCRFOAvailable.toString())

    FloatingActionButton(onClick = { scope.launch {if(isCRFOAvailable.value) viewModel.addContentReceivedFromOutsideAppInDb(context = context ) else viewModel.enableAddNewProjectDialog()} }) {
        if(isCRFOAvailable.value){
            Icon(imageVector = Icons.Default.Save, contentDescription = "Save Resource")

        }
        else{
            Icon(imageVector = Icons.Default.Create, contentDescription = "create Folder")
        }
    }
}