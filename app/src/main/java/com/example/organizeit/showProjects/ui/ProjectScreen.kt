package com.example.organizeit.showProjects.ui


import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.organizeit.ContentReceivedFromOutsideApp
import com.example.organizeit.showProjects.ShowProjectViewmodel
import kotlinx.serialization.Serializable

@Composable
fun ShowProjectView(navController: NavHostController) {
    val isCRFOAvailable= ContentReceivedFromOutsideApp.isCRFOAvailable.collectAsState()
    Scaffold(
        floatingActionButton = { if(isCRFOAvailable.value) else { FloatingActionButtonView() } }
    ) {padding->
        val viewModel: ShowProjectViewmodel = hiltViewModel()

        if(viewModel.isAddNewProjectDialogVisible.collectAsState().value){
            InputProjectNameView()
        }
        else{
            ProjectListView(navController)
        }
    }
}


@Serializable
object ScreenA

