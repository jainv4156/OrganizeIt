package com.example.organizeit.contentScreen.uiComponents.contentView

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.organizeit.contentScreen.ContentScreenViewModel
import com.example.organizeit.contentScreen.data.ContentSelectedLauncher
import com.example.organizeit.contentScreen.data.ProjectContentSelectedLauncher
import com.example.organizeit.contentScreen.data.ProjectContentTypeString
import com.example.organizeit.fileProviderUriConverter
import kotlinx.coroutines.launch

@Composable
fun AudioView(projectFolderId:String) {
    val viewModel:ContentScreenViewModel= hiltViewModel()
    val scope= rememberCoroutineScope()

    val audioPickerLauncher=rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = {
            scope.launch {
                viewModel.addContent(id=projectFolderId, uri = it)
            }
        }
    )
    val contentUri=viewModel.selectedContentUri.collectAsState()
    val context: Context = LocalContext.current

    LazyColumn {
        item{
        Button(onClick = {
            ProjectContentSelectedLauncher.AudioPickerLauncher.performAction(ContentSelectedLauncher.AudioPickerLauncher(audioPickerLauncher))
        }) {
            Text(text = "ClickME")
        }}
        items(contentUri.value.filter { it.type == ProjectContentTypeString.Audios.value }){ item->
            Text(text = item.toString()
                ,modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        val audioUri= fileProviderUriConverter(context,Uri.parse(item.uri))

                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            setDataAndType(audioUri, "audio/*")
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        context.startActivity(intent)
                    })
        }
    }

}