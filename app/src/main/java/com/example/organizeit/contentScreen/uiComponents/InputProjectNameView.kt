package com.example.organizeit.contentScreen.uiComponents


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.organizeit.contentScreen.ContentScreenViewModel
import kotlinx.coroutines.launch

@Composable

fun InputProjectNameView(){
    val viewModel: ContentScreenViewModel = hiltViewModel()
    val scope= rememberCoroutineScope()

    Box (
        modifier = Modifier
            .fillMaxSize()
            .padding(50.dp), contentAlignment = Alignment.Center){
        Column {
            OutlinedTextField(value = viewModel.folderName.collectAsState().value, onValueChange ={viewModel.setProjectName(it)} )
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Button(modifier = Modifier
                    .padding( 16.dp),onClick = { scope.launch {  viewModel.disableAddNewProjectDialog()}}) {
                    Text(text = "Cancel")
                }
                Button(modifier = Modifier
                    .padding( 16.dp),onClick = { scope.launch {  viewModel.createFolder()}}) {
                    Text(text = "Create")
                }
            }
        }
    }

}