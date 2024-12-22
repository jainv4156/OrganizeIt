package com.example.organizeit.contentScreen.uiComponents

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.organizeit.contentScreen.ContentScreenViewModel
import com.example.organizeit.contentScreen.data.ProjectContentTypeString
import com.example.organizeit.contentScreen.uiComponents.contentView.AudioView
import com.example.organizeit.contentScreen.uiComponents.contentView.DocumentView
import com.example.organizeit.contentScreen.uiComponents.contentView.PhotoView
import com.example.organizeit.contentScreen.uiComponents.contentView.VideoView

@Composable
fun ShowContentView(projectFolderId:String){
    val viewModel: ContentScreenViewModel = hiltViewModel()

    if(viewModel.projectContentTypeViewOnScreen.collectAsState().value== ProjectContentTypeString.Photos.value){
        PhotoView(projectFolderId = projectFolderId)
    }
    if(viewModel.projectContentTypeViewOnScreen.collectAsState().value== ProjectContentTypeString.Videos.value){
        VideoView(projectFolderId = projectFolderId)
    }
    if(viewModel.projectContentTypeViewOnScreen.collectAsState().value==ProjectContentTypeString.Audios.value){
        AudioView(projectFolderId = projectFolderId)
    }
    if(viewModel.projectContentTypeViewOnScreen.collectAsState().value==ProjectContentTypeString.Documents.value){
        DocumentView(projectFolderId = projectFolderId)
    }

}