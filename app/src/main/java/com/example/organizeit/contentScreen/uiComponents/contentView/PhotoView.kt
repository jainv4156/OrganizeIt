package com.example.organizeit.contentScreen.uiComponents.contentView

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.organizeit.contentScreen.ContentScreenViewModel
import com.example.organizeit.contentScreen.data.ContentSelectedLauncher
import com.example.organizeit.contentScreen.data.ProjectContentSelectedLauncher
import com.example.organizeit.contentScreen.data.ProjectContentTypeString
import kotlinx.coroutines.launch

@Composable
fun PhotoView(projectFolderId:String) {
    val viewModel:ContentScreenViewModel= hiltViewModel()
    val scope= rememberCoroutineScope()
    val photoPickerLauncher=rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
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
            Button(onClick = { ProjectContentSelectedLauncher.PhotoPickerLauncher.performAction(ContentSelectedLauncher.PhotoPickerLauncher(photoPickerLauncher))}) {
                Text(text = "ClickME")
            }}
        items(contentUri.value.filter { it.type == ProjectContentTypeString.Photos.value }){item->

            Text(text = item.uri,Modifier.clickable {  val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(Uri.parse(item.uri), "image/*")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
                context.startActivity(intent) })
            AsyncImage(model = Uri.parse(item.uri),
                contentDescription = "Photos",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            setDataAndType(Uri.parse(item.uri), "image/*")
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        context.startActivity(intent)
                    })

        }
    }

}