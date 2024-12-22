package com.example.organizeit.contentScreen.uiComponents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.organizeit.ContentReceivedFromOutsideApp
import com.example.organizeit.contentScreen.ContentScreenViewModel
import com.example.organizeit.contentScreen.data.ProjectContentTypeString
import com.example.organizeit.contentScreen.uiComponents.folderSection.FolderSectionView
import kotlinx.serialization.Serializable

@Composable
fun ProjectScreenView(parentFolderId: String, navController: NavHostController) {
    val viewModel:ContentScreenViewModel= hiltViewModel()
    val isCRFOAvailable=ContentReceivedFromOutsideApp.isCRFOAvailable.collectAsState()
    viewModel.setParentFolderId(parentFolderId)
    LaunchedEffect (Unit){
        viewModel.fillInitialProjectPhotoData(parentFolderId)
    }
    Scaffold(floatingActionButton = { FloatingActionButtonViewForAddingFolder() }) { padding->
        val isAddNewProjectDialogVisible=viewModel.isAddNewFolderDialogVisible.collectAsState()
        if(isAddNewProjectDialogVisible.value){
            InputProjectNameView()
        }
        else{
            Column {
                if(!isCRFOAvailable.value){

                    Row {
                        Button(onClick = { viewModel.setProjectContentTypeViewOnScreen(ProjectContentTypeString.Photos.value) }) {
                            Text(text = "Photos")
                        }
                        Button(onClick = { viewModel.setProjectContentTypeViewOnScreen(ProjectContentTypeString.Audios.value)}) {
                            Text(text = "Audios")
                        }
                        Button(onClick = {viewModel.setProjectContentTypeViewOnScreen(ProjectContentTypeString.Videos.value) }) {
                            Text(text = "Videos")
                        }
                        Button(onClick = { viewModel.setProjectContentTypeViewOnScreen(ProjectContentTypeString.Documents.value)}) {
                            Text(text = "Documents")
                        }
                    }
                    Row {
                        Button(onClick = { /*TODO*/ }) {
                          Text(text = "Camera")
                        }
                        Button(onClick = { /*TODO*/ }) {
                            Text(text = "Audio " )
                        }
                    }
                }
                FolderSectionView(navController = navController)
                if(!isCRFOAvailable.value){
                    ShowContentView(projectFolderId = parentFolderId)
                }
            }
        }
    }
}
@Serializable
data class ScreenB(
    val parentFolderId:String=""
)