package com.example.organizeit.showProjects.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.organizeit.showProjects.ShowProjectViewmodel
import kotlinx.coroutines.launch

@Composable
fun FloatingActionButtonView(){
    val viewModel: ShowProjectViewmodel = hiltViewModel()
    val scope= rememberCoroutineScope()

    FloatingActionButton(onClick = { scope.launch {  viewModel.enableAddNewProjectDialog()} }) {
        Icon(imageVector = Icons.Default.Create, contentDescription = "create Projects")
    }
}