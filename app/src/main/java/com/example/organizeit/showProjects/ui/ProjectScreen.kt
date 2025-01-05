package com.example.organizeit.showProjects.ui


import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.organizeit.CameraScreen.ui.ScreenC
import com.example.organizeit.ContentReceivedFromOutsideApp
import com.example.organizeit.ContentReceivedFromCamera
import com.example.organizeit.showProjects.ShowProjectViewmodel
import kotlinx.serialization.Serializable

@Composable
fun ShowProjectView(navController: NavHostController) {
    val isCRFOAvailable= ContentReceivedFromOutsideApp.isCRFOAvailable.collectAsState()
    val isPRFCAvailable= ContentReceivedFromCamera.isCRFCAvailable.collectAsState()
    val isSaveViewActive=!isCRFOAvailable.value && !isPRFCAvailable.value

    Scaffold(floatingActionButton = { if(isSaveViewActive) FloatingActionButtonView()}
    ) {padding->
        val viewModel: ShowProjectViewmodel = hiltViewModel()
        Column {
            Button(onClick = {navController.navigate(ScreenC)}) {
                Text(text = "Camera")
            }
            if(viewModel.isAddNewProjectDialogVisible.collectAsState().value){
                InputProjectNameView()
            }
            else{
                ProjectListView(navController)
            }

        }

    }
}


@Serializable
object ScreenA

